package com.example.gym.home

import com.example.gym.mvi.BaseViewModel
import com.example.gym.mvi.MviStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    store: MviStore<HomeState>
) : BaseViewModel<HomeState>(store)