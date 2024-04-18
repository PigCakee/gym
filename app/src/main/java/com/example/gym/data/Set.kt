package com.example.gym.data

data class Set(
    val sugWeight: Double,
    val sugReps: Double,
    val restTimer: Long = DEFAULT_TIMER,
    val actualWeight: Double? = null,
    val actualReps: Double? = null,
) {
    companion object {
        const val DEFAULT_TIMER = 180000L
    }
}

data class SuggestedSet(val weight: Double, val reps: Int)
