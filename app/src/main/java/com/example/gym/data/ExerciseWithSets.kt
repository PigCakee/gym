package com.example.gym.data

import com.example.gym.components.ExerciseUi

data class ExerciseWithSets(
    val exercise: ExerciseUi,
    val sets: List<Set>
)
