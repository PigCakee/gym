package com.example.gym.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gym.R

const val NO_ID = "NO_ID"

sealed class NavigationDirections(
    val destination: String = ""
) {
    data object Stub : NavigationDirections()
    data object Home : NavigationDirections(destination = "welcome")
    data object NewSession : NavigationDirections(destination = "new_session")
    data object Session : NavigationDirections(destination = "session")
    data object History : NavigationDirections(destination = "history")
    data object Progress : NavigationDirections(destination = "progress")

    companion object {
        @Composable
        fun getBottomNavigationItems(): List<BottomNavigationItem> {
            return listOf(
                BottomNavigationItem(
                    label = stringResource(id = R.string.title_home),
                    icon = painterResource(id = R.drawable.ic_courses),
                    destination = Home.destination
                ),
                BottomNavigationItem(
                    label = stringResource(id = R.string.title_history),
                    icon = painterResource(id = R.drawable.ic_courses),
                    destination = History.destination
                ),
                BottomNavigationItem(
                    label = stringResource(id = R.string.title_progress),
                    icon = painterResource(id = R.drawable.ic_courses),
                    destination = Progress.destination
                ),
            )
        }
    }
}
