package com.example.gym.new_session

import com.example.gym.components.ExerciseUi
import com.example.gym.data.ExerciseWithSets
import com.example.gym.data.Set
import com.example.gym.mvi.MviIntent
import com.example.gym.mvi.MviState

sealed interface NewSessionIntent : MviIntent {
    data class Init(val state: NewSessionState?) : NewSessionIntent
    data class LoadExercises(val exercises: List<ExerciseUi>) : NewSessionIntent
    data class AddExercise(val exercise: ExerciseUi) : NewSessionIntent
    data class ExerciseAdded(val exerciseWithSets: ExerciseWithSets) : NewSessionIntent
    data class StartSetTimer(val exercise: ExerciseUi, val set: Set, val index: Int) : NewSessionIntent
    data class ModifySet(val exercise: ExerciseUi, val set: Set, val index: Int) : NewSessionIntent
    data class OnQueryChanged(val query: String) : NewSessionIntent
}

data class NewSessionState(
    val isLoading: Boolean = false,
    val query: String = "",
    val exercisesWithSets: List<ExerciseWithSets> = emptyList(),
    val availableExercises: List<ExerciseUi> = emptyList()
) : MviState