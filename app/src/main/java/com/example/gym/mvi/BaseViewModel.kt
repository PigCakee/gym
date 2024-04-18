package com.example.gym.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State : MviState>(
    private val store: MviStore<State>
) : ViewModel() {

    val state = store.state.asStateFlow()

    init {
        store.init(viewModelScope)
    }

    fun acceptIntent(intent: MviIntent) {
        store.intents.tryEmit(intent)
    }
}