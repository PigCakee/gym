package com.example.gym.new_session

import com.example.gym.components.ExerciseUi
import com.example.gym.data.Exercise
import com.example.gym.data.ExerciseWithSets
import com.example.gym.data.Set
import com.example.gym.mvi.Middleware
import com.example.gym.mvi.MviIntent
import com.example.gym.room.dao.ExercisesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewSessionMiddleware @Inject constructor(
    private val exercisesDao: ExercisesDao
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
                    sets = loadSets(exercise)
                )
            )
        )
    }

    private suspend fun loadSets(exercise: Exercise): List<Set> {
        return listOf(
            Set(
                sugWeight = getSuggestedWeightForExercise(exercise, 1),
                sugReps = getSuggestedWeightForExercise(exercise, 1)
            ),
            Set(
                sugWeight = getSuggestedWeightForExercise(exercise, 2),
                sugReps = getSuggestedWeightForExercise(exercise, 2)
            ),
            Set(
                sugWeight = getSuggestedWeightForExercise(exercise, 3),
                sugReps = getSuggestedWeightForExercise(exercise, 3)
            ),
        )
    }

    private suspend fun getSuggestedWeightForExercise(exercise: Exercise, setNum: Int): Double {
        return 45.0
    }

    private suspend fun getSuggestedRepsForExercise(exercise: Exercise, setNum: Int): Double {
        return 7.0
    }
}