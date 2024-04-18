package com.example.gym.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gym.components.GymButton
import com.example.gym.mvi.MviIntent
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.White

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
    Scaffold(
        contentColor = GymTheme.colors.navBar,
        containerColor = White,
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            GymButton(
                modifier = Modifier.align(Alignment.Center),
                buttonText = "Start a session",
                onClick = {
                    onIntent(HomeIntent.StartSession)
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    GymTheme {
        HomeContent(state = HomeState(), onIntent = {})
    }
}