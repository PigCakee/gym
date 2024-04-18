package com.example.gym.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

data class UiText(@StringRes val textId: Int? = null, val textStr: String? = null) {
    fun asString(context: Context): String {
        return if (textId != null) {
            context.getString(textId)
        } else textStr ?: ""
    }
}

fun Context.getString(uiText: UiText) = uiText.asString(this)
fun Fragment.getString(uiText: UiText) = uiText.asString(requireContext())