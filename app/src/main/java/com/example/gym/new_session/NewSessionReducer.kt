package com.example.gym.new_session

import com.example.gym.mvi.MviIntent
import com.example.gym.mvi.Reducer
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NewSessionReducer @Inject constructor() : Reducer<NewSessionState> {
    override fun reduce(state: NewSessionState, intent: MviIntent): NewSessionState {
        return when (intent) {
            is NewSessionIntent.Init -> {
                intent.state ?: state.copy(isLoading = true)
            }

            is NewSessionIntent.ExerciseAdded -> {
                state.copy(
                    exercisesWithSets = state.exercisesWithSets.plus(intent.exerciseWithSets)
                )
            }

            is NewSessionIntent.LoadExercises -> {
                state.copy(
                    availableExercises = intent.exercises
                )
            }

            is NewSessionIntent.OnQueryChanged -> {
                state.copy(
                    query = intent.query
                )
            }

            is NewSessionIntent.ModifySet -> {
                val index = state.exercisesWithSets.indexOf(
                    state.exercisesWithSets.first { it.exercise.id == intent.exercise.id }
                )
                val newList = state.exercisesWithSets.toMutableList()
                val newSetList = newList[index].sets.toMutableList()
                newSetList[intent.index] = intent.set
                newList[index] = newList[index].copy(
                    sets = newSetList
                )
                state.copy(
                    exercisesWithSets = newList
                )
            }

            else -> state
        }
    }
}