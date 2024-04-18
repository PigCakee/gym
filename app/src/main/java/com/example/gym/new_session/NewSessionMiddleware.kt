package com.example.gym.new_session

import com.example.gym.components.ExerciseUi
import com.example.gym.data.Exercise
import com.example.gym.data.ExerciseWithSets
import com.example.gym.data.Session
import com.example.gym.data.Set
import com.example.gym.data.SuggestedSet
import com.example.gym.mvi.Middleware
import com.example.gym.mvi.MviIntent
import com.example.gym.nav.Back
import com.example.gym.nav.NavigationManager
import com.example.gym.room.dao.ExercisesDao
import com.example.gym.room.dao.SessionsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                                        availableExercises = exercisesDao.getAllExercises().map {
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
                        exercisesDao.getAllExercises().find { it.id == intent.exercise.id }?.let {
                            addExercise(it, outputIntents)
                        }
                    }
                }

                is NewSessionIntent.OnQueryChanged -> {
                    coroutineScope.launch(Dispatchers.IO) {
                        outputIntents.emit(
                            NewSessionIntent.LoadExercises(
                                exercises = exercisesDao.getExercisesWithName(intent.query).map {
                                    it.mapToUi()
                                }
                            )
                        )
                    }
                }

                is NewSessionIntent.StartSetTimer -> {
                    coroutineScope.launch(Dispatchers.IO) {
                        while (intent.set.restTimer > 0L) {
                            outputIntents.emit(
                                NewSessionIntent.ModifySet(
                                    exercise = intent.exercise,
                                    set = intent.set.copy(restTimer = intent.set.restTimer - 1000L),
                                    index = intent.index
                                )
                            )
                            delay(1000L)
                        }
                    }
                    // TODO send notification
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
            SuggestedSet(increasedAverageWeight, suggestedReps)
        }.map {
            it.copy(weight = "%.3f".format(it.weight).toDouble())
        }
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