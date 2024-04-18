package com.example.gym.home

import android.content.Context
import androidx.navigation.NavDirections
import com.example.gym.R
import com.example.gym.data.Exercise
import com.example.gym.mvi.Middleware
import com.example.gym.mvi.MviIntent
import com.example.gym.nav.Forward
import com.example.gym.nav.NavigationDirections
import com.example.gym.nav.NavigationManager
import com.example.gym.room.dao.ExercisesDao
import com.example.gym.room.fromJson
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class HomeMiddleware @Inject constructor(
    private val exercisesDao: ExercisesDao,
    private val navigationManager: NavigationManager
) : Middleware<HomeState> {

    override fun execute(
        intent: MviIntent,
        state: HomeState,
        outputIntents: MutableSharedFlow<MviIntent>,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            when (intent) {
                is HomeIntent.Init -> {
                    if (intent.state == null) {
                        outputIntents.emit(
                            HomeIntent.Init(
                                state.copy(),
                            )
                        )
                    }
                }

                is HomeIntent.CheckDb -> {
                    coroutineScope.launch(Dispatchers.IO) {
                        populateDatabase(intent.context)
                    }
                }

                is HomeIntent.StartSession -> {
                    navigationManager.navigate(Forward(NavigationDirections.NewSession))
                }
            }
        }
    }

    private suspend fun populateDatabase(context: Context) {
        if (exercisesDao.getExerciseCount() == 0) {
            val inputStream = context.resources.openRawResource(R.raw.exercises)
            val reader = BufferedReader(InputStreamReader(inputStream))

            val exercisesJson = reader.readText()

            val exerciseList: List<Exercise> = Gson().fromJson(exercisesJson)

            withContext(Dispatchers.Main) {
                exerciseList.forEach { exercise ->
                    exercisesDao.insertExercise(exercise)
                }
            }
        }
    }
}