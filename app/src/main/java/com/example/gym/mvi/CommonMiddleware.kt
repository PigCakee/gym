package com.example.gym.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommonMiddleware @Inject constructor(
): Middleware<MviState> {

    override fun execute(
        intent: MviIntent,
        state: MviState,
        outputIntents: MutableSharedFlow<MviIntent>,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            when (intent) {
                is CommonIntent.NavigateUp -> {
                }

                is CommonIntent.OpenEmail -> {
                }

                is CommonIntent.OpenUrl -> {
                }

                is CommonIntent.ShareCode -> {
                }
            }
        }
    }
}