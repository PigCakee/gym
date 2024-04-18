package com.example.gym.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gym.components.GymButton
import com.example.gym.mvi.MviIntent
import com.example.gym.ui.theme.GymTheme

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.acceptIntent(HomeIntent.CheckDb(context))
    }
    HomeContent(
        state = state,
        onIntent = viewModel::acceptIntent
    )
}

@Composable
fun HomeContent(
    state: HomeState,
    onIntent: (MviIntent) -> Unit
) {
    Column {
        GymButton(buttonText = "Start a session", onClick = {
            onIntent(HomeIntent.StartSession)
        })
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    GymTheme {
        HomeContent(state = HomeState(), onIntent = {})
    }
}