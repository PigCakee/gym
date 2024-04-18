package com.example.gym.new_session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gym.components.ExerciseBlock
import com.example.gym.components.ExercisePickerBottomSheet
import com.example.gym.components.GymPlus
import com.example.gym.components.SetBlock
import com.example.gym.components.SetTimer
import com.example.gym.mvi.MviIntent
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun NewSessionScreen(viewModel: NewSessionViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    NewSessionContent(
        state = state,
        onIntent = viewModel::acceptIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSessionContent(
    state: NewSessionState,
    onIntent: (MviIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        contentColor = GymTheme.colors.navBar,
        containerColor = White,
        topBar = {
//            LimeTopAppBar(
//                title = stringResource(id = R.string.title_profile),
//                onNavigateUpClick = { onIntent(CommonIntent.NavigateUp) }
//            )
        }
    ) { paddingValues ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (state.exercisesWithSets.isEmpty()) {
                item {
                    GymPlus(
                        onClick = { showBottomSheet = true },
                        modifier = Modifier.size(64.dp),
                    )
                }
            } else {
                items(state.exercisesWithSets) { exerciseWithSets ->
                    ExerciseBlock(exerciseUi = exerciseWithSets.exercise)
                    Spacer(modifier = Modifier.height(8.dp))
                    exerciseWithSets.sets.forEachIndexed { index, set ->
                        SetBlock(
                            set = set,
                            setNum = index + 1,
                            onModify = {
                                onIntent(
                                    NewSessionIntent.ModifySet(
                                        exercise = exerciseWithSets.exercise,
                                        set = it,
                                        index = index
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (set.restTimer > 0L) {
                            SetTimer(
                                modifier = Modifier.wrapContentSize(),
                                millis = set.restTimer,
                                onStart = {
                                    onIntent(
                                        NewSessionIntent.StartSetTimer(
                                            exercise = exerciseWithSets.exercise,
                                            set = set,
                                            index = index
                                        )
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                item {

                }
            }
        }
    }

    if (showBottomSheet) {
        ExercisePickerBottomSheet(
            query = state.query,
            exercises = state.availableExercises,
            sheetState = sheetState,
            onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            },
            onPicked = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
                onIntent(NewSessionIntent.AddExercise(it))
            },
            onQueryChanged = { onIntent(NewSessionIntent.OnQueryChanged(it)) }
        )
    }
}

@Preview
@Composable
fun PreviewNewSessionScreen() {
    GymTheme {
        NewSessionContent(state = NewSessionState(), onIntent = {})
    }
}