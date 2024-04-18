package com.example.gym.nav

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {

    private val commandsChannel = Channel<NavigationCommand>(Channel.BUFFERED)
    val commands = commandsChannel.receiveAsFlow()

    private var resultListener: ((NavigationResult?) -> Unit)? = null

    fun navigate(command: NavigationCommand) {
        commandsChannel.trySend(command)
    }

    fun navigateForResult(command: NavigationCommand, callback: (NavigationResult?) -> Unit) {
        resultListener = callback
        navigate(command)
    }

    fun backWithResult(result: NavigationResult?) {
        navigate(Back)
        resultListener?.invoke(result)
        resultListener = null
    }
}

interface NavigationResult

fun <T : NavigationResult> NavigationResult?.proceedSuccessResult(body: (T) -> Unit) {
    (this as? T)?.let { body(it) }
}
