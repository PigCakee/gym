package com.example.gym.mvi

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

interface MviIntent
interface MviState

// generic intent, we can define any other, but they need to inherit from MviIntent
sealed interface GenericIntent : MviIntent {
    data object Init : GenericIntent
    data object OnCleared : GenericIntent
    data class Loading(val isLoading: Boolean) : GenericIntent
    data class Failure(
        val error: Throwable,
        val showAlert: Boolean = true
    ) : GenericIntent
}

interface Reducer<State : MviState> {
    fun reduce(state: State, intent: MviIntent): State
}

interface Middleware<State : MviState> {
    fun execute(
        intent: MviIntent,
        state: State,
        outputIntents: MutableSharedFlow<MviIntent>,
        coroutineScope: CoroutineScope
    )

    fun genericExceptionHandler(
        outputIntents: MutableSharedFlow<MviIntent>,
        showError: Boolean = true
    ) = CoroutineExceptionHandler { _, exception ->
        //Timber.e(exception, "Generic CoroutineExceptionHandler got $exception")
        if (exception !is CancellationException) {
            outputIntents.tryEmit(GenericIntent.Failure(exception, showError))
        }
    }

    fun CoroutineScope.launch(
        output: MutableSharedFlow<MviIntent>,
        handleErrors: Boolean = true,
        showAlert: Boolean = true,
        handleLoading: Boolean = true,
        body: suspend CoroutineScope.() -> Unit
    ) {
        launch(
            context = if (handleErrors) genericExceptionHandler(
                outputIntents = output,
                showError = showAlert
            ) else EmptyCoroutineContext
        ) {
            if (handleLoading) output.tryEmit(GenericIntent.Loading(true))
            body()
            if (handleLoading) output.tryEmit(GenericIntent.Loading(false))
        }
    }
}

class MviStore<State : MviState>(
    private val initialState: State,
    private val reducer: Reducer<State>,
    private val middlewares: Set<Middleware<State>>,
    private val commonMiddlewares: Set<Middleware<MviState>>
) {

    val state = MutableStateFlow(initialState)
    val intents = MutableSharedFlow<MviIntent>(extraBufferCapacity = 100)

    fun init(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            intents
                .onSubscription { intents.emit(GenericIntent.Init) }
                .onCompletion {
                    executeIntent(GenericIntent.OnCleared, coroutineScope)
                    state.value = initialState
                }
                .collect { executeIntent(it, coroutineScope) }
        }
    }

    private fun executeIntent(intent: MviIntent, coroutineScope: CoroutineScope) {
        //Timber.d(generateActionLogs(intent, initialState))

        state.update { old ->
            // responsible for state management based on intents
            reducer.reduce(old, intent)
        }

        middlewares.forEach {
            // responsible for handling incoming intents based on current state, able to push its own using "intents"
            it.execute(intent, state.value, intents, coroutineScope)
        }

        commonMiddlewares.forEach {
            // responsible for handling incoming intents based on current state, able to push its own using "intents"
            // typically used across multiple screens
            it.execute(intent, state.value, intents, coroutineScope)
        }
    }
}

inline fun <reified T> MviIntent.tryCast(block: T.() -> Unit) {
    if (this is T) block()
}

private fun generateActionLogs(intent: MviIntent, state: MviState) =
    "ACTION: ${intent.javaClass.simpleName} " +
            when (intent) {
                is GenericIntent.Init, is GenericIntent.OnCleared -> state.javaClass.simpleName
                is GenericIntent.Loading -> {
                    intent.isLoading
                }

                else -> ""
            }
