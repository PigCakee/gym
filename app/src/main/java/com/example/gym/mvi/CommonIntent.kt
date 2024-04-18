package com.example.gym.mvi

sealed interface CommonIntent: MviIntent {
    data object NavigateUp : CommonIntent
    data class OpenUrl(val url: String) : CommonIntent
    data class OpenEmail(val email: String) : CommonIntent
    data class ShareCode(val code: String) : CommonIntent
    data class OpenInfoDialog(val title: String, val body: String) : CommonIntent
}