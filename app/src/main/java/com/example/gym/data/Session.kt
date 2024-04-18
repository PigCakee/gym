package com.example.gym.data

import androidx.room.PrimaryKey

data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: String,

    val date: String,
)

