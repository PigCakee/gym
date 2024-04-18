package com.example.gym.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val date: String,
    val exercisesWithSets: List<ExerciseWithSets>
)