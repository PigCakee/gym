package com.example.gym.nav

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.gym.R
import com.example.gym.util.UiText

sealed class NavigationCommand(val directions: NavigationDirections = NavigationDirections.Stub)

class Forward(directions: NavigationDirections, val singleTop: Boolean = false) : NavigationCommand(directions)
class CleanForward(directions: NavigationDirections, val singleTop: Boolean = false) : NavigationCommand(directions)
class Replace(directions: NavigationDirections, val singleTop: Boolean = false) : NavigationCommand(directions)
data object Back : NavigationCommand()
class BackTo(directions: NavigationDirections) : NavigationCommand(directions)

// Custom
data class OpenAlertDialog(
    //val dialog: Dialog = genericErrorDialog,
    val onPositiveButtonClick: () -> Unit = {},
    val onNegativeButtonClick: () -> Unit = {},
    val onDismiss: () -> Unit = {}
) : NavigationCommand()

data class ShowMessage(
    val message: UiText,
    val messageType: MessageType
) : NavigationCommand() {
    sealed class MessageType {
        data object Positive : MessageType()
        data object Negative : MessageType()
        data object Warning : MessageType()

        fun getBannerStyle(): BannerStyle {
            return when (this) {
                is Positive -> BannerStyle(
                    strokeColor = Color(0xFF23AC00),
                    backgroundColor = Color(0xFFEBFFE5),
                    resId = R.drawable.ic_banner_positive
                )
                is Negative -> BannerStyle(
                    strokeColor = Color(0xFFEF476F),
                    backgroundColor = Color(0xFFFFEDF1),
                    resId = R.drawable.ic_banner_negative
                )
                is Warning -> BannerStyle(
                    strokeColor = Color(0xFF23AC00),
                    backgroundColor = Color(0xFFEBFFE5),
                    resId = R.drawable.ic_banner_positive
                )
            }
        }
    }
}

data class OpenBrowser(val url: String) : NavigationCommand()
data class OpenEmail(val email: String) : NavigationCommand()
data class ShareCode(val code: String) : NavigationCommand()

data class BannerStyle(
    val strokeColor: Color,
    val backgroundColor: Color,
    @DrawableRes val resId: Int
)