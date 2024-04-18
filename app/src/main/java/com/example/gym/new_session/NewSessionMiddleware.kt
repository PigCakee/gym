package com.example.gym.new_session

import android.os.CountDownTimer
import com.example.gym.components.ExerciseUi
import com.example.gym.data.Exercise
import com.example.gym.data.ExerciseWithSets
import com.example.gym.data.Session
import com.example.gym.data.Set
import com.example.gym.data.SuggestedSet
import com.example.gym.mvi.CommonIntent
import com.example.gym.mvi.Middleware
import com.example.gym.mvi.MviIntent
import com.example.gym.nav.Back
import com.example.gym.nav.NavigationManager
import com.example.gym.nav.ShowMessage
import com.example.gym.room.dao.ExercisesDao
import com.example.gym.room.dao.SessionsDao
import com.example.gym.util.debugLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class NewSessionMiddleware @Inject constructor(
    private val exercisesDao: ExercisesDao,
    private val sessionsDao: SessionsDao,
    private val navigationManager: NavigationManager
) : Middleware<NewSessionState> {

    var queryJob: Job? = null

    override fun execute(
        intent: MviIntent,
        state: NewSessionState,
        outputIntents: MutableSharedFlow<MviIntent>,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            when (intent) {
                is NewSessionIntent.Init -> {
                    if (intent.state == null) {
                        coroutineScope.launch(Dispatchers.IO) {
                            outputIntents.emit(
                                NewSessionIntent.Init(
                                    state.copy(
                                        availableExercises = exercisesDao.getRecentExercises()
                                            .ifEmpty {
                                                exercisesDao.getAllExercises()
                                            }
                                            .map {
                                                it.mapToUi()
                                            }
                                    )
                                )
                            )
                        }
                    }
                }

                is NewSessionIntent.AddExercise -> {
                    coroutineScope.launch(Dispatchers.IO) {
                        val exercise = exercisesDao.getExercisesById(intent.exercise.id)
                        addExercise(exercise, outputIntents)
                    }
                }

                is NewSessionIntent.OnQueryChanged -> {
                    queryJob?.cancel()
                    queryJob = coroutineScope.launch(Dispatchers.IO) {
                        outputIntents.emit(
                            NewSessionIntent.LoadExercises(
                                exercises = (
                                    if (intent.query.isNotBlank()) {
                                        exercisesDao.getExercisesWithName(intent.query)
                                    } else {
                                        exercisesDao.getRecentExercises()
                                    }
                                ).map { it.mapToUi() }
                            )
                        )
                    }
                }

                is NewSessionIntent.StartSetTimer -> {
                    val newSet = intent.set.copy(restTimer = intent.set.restTimer)
                    object : CountDownTimer(newSet.restTimer, 1000L) {
                        override fun onTick(millisUntilFinished: Long) {
                            coroutineScope.launch {
                                outputIntents.emit(
                                    NewSessionIntent.ModifySet(
                                        exercise = intent.exercise,
                                        set = newSet.copy(restTimer = millisUntilFinished),
                                        index = intent.index
                                    )
                                )
                            }
                        }

                        override fun onFinish() {
                            coroutineScope.launch {
                                outputIntents.emit(
                                    NewSessionIntent.ModifySet(
                                        exercise = intent.exercise,
                                        set = newSet.copy(restTimer = 0L),
                                        index = intent.index
                                    )
                                )
                                outputIntents.emit(CommonIntent.SendNotification)
                            }
                        }
                    }.start()
                }

                is NewSessionIntent.FinishSession -> {
                    sessionsDao.insertSession(
                        Session(
                            id = UUID.randomUUID().mostSignificantBits,
                            date = Date().toString(),
                            exercisesWithSets = state.exercisesWithSets
                        )
                    )
                    navigationManager.navigate(Back)
                }
            }
        }
    }

    private suspend fun addExercise(
        exercise: Exercise,
        outputIntents: MutableSharedFlow<MviIntent>
    ) {
        exercisesDao.insertExercise(exercise.copy(recentlyAdded = true))
        outputIntents.emit(
            NewSessionIntent.ExerciseAdded(
                ExerciseWithSets(
                    exercise = exercise.mapToUi(),
                    sets = loadSets(exercise.mapToUi())
                )
            )
        )
    }

    private suspend fun loadSets(exercise: ExerciseUi): List<Set> {
        return calculateProgressiveOverload(
            history = sessionsDao.getAllSessions()
                .flatMap { session -> session.exercisesWithSets }
                .filter { exerciseWithSets -> exercise.id == exerciseWithSets.exercise.id },
            recentSessions = 3
        ).map {
            Set(
                sugWeight = it.weight,
                sugReps = it.reps.toDouble()
            )
        }
    }

    private fun calculateProgressiveOverload(
        history: List<ExerciseWithSets>,
        recentSessions: Int = 3
    ): List<SuggestedSet> {
        // Get the last 'recentSessions' sessions
        val recentHistory = history.takeLast(recentSessions)
        if (recentHistory.size < recentSessions) return List(3) {
            SuggestedSet(
                weight = 0.0,
                reps = 0
            )
        }

        // Calculate average weight and reps for each set from recent history
        val averageReps = mutableListOf<Double>()

        for (i in 0 until 3) { // Assuming each session has 3 sets
            val setReps = recentHistory.mapNotNull { session ->
                session.sets.getOrNull(i)?.actualReps
            }

            averageReps.add(calculateNextReps(setReps))
        }

        val initialAverageWeight =
            recentHistory.flatMap { it.sets.map { set -> set.actualWeight ?: 0.0 } }.average()
        val increasedAverageWeight =
            if (averageReps.last() >= 8.0) initialAverageWeight * 1.05 else initialAverageWeight

        return List(3) { index ->
            val suggestedReps =
                if (averageReps.last() >= 8) averageReps[index].toInt() else averageReps[index].toInt() + 1
            SuggestedSet(roundToNearestValidValue(increasedAverageWeight), suggestedReps)
        }.map {
            it.copy(weight = "%.3f".format(it.weight).toDouble())
        }
    }

    private fun roundToNearestValidValue(number: Double): Double {
        val multipliedNumber = number * 1000
        val roundedInteger = (multipliedNumber + 0.5).toInt()  // Adding 0.5 for correct rounding
        val integerPart = roundedInteger / 1000
        val decimalPart = roundedInteger % 1000

        val adjustedDecimalPart = when {
            decimalPart <= 0 || decimalPart > 825 -> 0
            decimalPart <= 125 -> 125
            decimalPart <= 250 -> 250
            decimalPart <= 375 -> 375
            decimalPart <= 500 -> 500
            decimalPart <= 625 -> 625
            decimalPart <= 750 -> 750
            else -> 825
        }

        return (integerPart * 1000 + adjustedDecimalPart) / 1000.0
    }

    private fun calculateNextReps(reps: List<Double>): Double {
        // Increase reps by 1 if the average reps in the last session was less than 8
        // Otherwise, keep the same reps
        val averageReps = reps.average()

        return if (averageReps < 8.0) {
            averageReps + 1
        } else {
            averageReps
        }
    }
}