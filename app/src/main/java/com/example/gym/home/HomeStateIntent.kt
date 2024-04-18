package com.example.gym.home

import android.content.Context
import com.example.gym.mvi.MviIntent
import com.example.gym.mvi.MviState

sealed interface HomeIntent : MviIntent {
    data class Init(val state: HomeState?) : HomeIntent
    data class CheckDb(val context: Context) : HomeIntent
    data object StartSession : HomeIntent
}

data class HomeState(
    val isLoading: Boolean = false,
) : MviState