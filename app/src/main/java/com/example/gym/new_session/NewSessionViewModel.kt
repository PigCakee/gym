package com.example.gym.new_session

import com.example.gym.mvi.BaseViewModel
import com.example.gym.mvi.MviStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewSessionViewModel @Inject constructor(
    store: MviStore<NewSessionState>
) : BaseViewModel<NewSessionState>(store) {
    init {
        acceptIntent(NewSessionIntent.Init(null))
    }
}