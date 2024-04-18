package com.example.gym.home

import com.example.gym.mvi.MviIntent
import com.example.gym.mvi.Reducer
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class HomeReducer @Inject constructor() : Reducer<HomeState> {
    override fun reduce(state: HomeState, intent: MviIntent): HomeState {
        return when (intent) {
            is HomeIntent.Init -> {
                intent.state ?: state.copy(isLoading = true)
            }

            else -> state
        }
    }
}