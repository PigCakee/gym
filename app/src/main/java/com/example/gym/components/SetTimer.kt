package com.example.gym.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.ui.theme.GymTheme
import kotlin.math.floor

@Composable
fun SetTimer(
    millis: Long,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isStarted by remember {
        mutableStateOf(false)
    }

    Surface(
        border = BorderStroke(1.dp, GymTheme.colors.uiBorder),
        shape = CircleShape,
        color = Color.White
    ) {
        Box(modifier = modifier) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        if (!isStarted) {
                            onStart()
                            isStarted = true
                        }
                    }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (isStarted) {
                    Text(
                        text = "Rest",
                        style = GymTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GymTheme.colors.textPrimary
                        )
                    )
                    val time = getMinutesAndSeconds(millis)
                    Text(
                        text = "${time.first}:${time.second}",
                        style = GymTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GymTheme.colors.textPrimary
                        )
                    )
                } else {
                    Text(
                        text = "Finished",
                        style = GymTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GymTheme.colors.textPrimary
                        )
                    )
                }
            }
        }
    }
}

private fun getMinutesAndSeconds(millisUntilFinished: Long): Pair<String, String> {
    val timeInSeconds = millisUntilFinished / 1000.0
    val secondsLeft = (timeInSeconds % 3600 % 60).toInt()
    val minutesLeft = floor(timeInSeconds % 3600 / 60).toInt()

    val minutes = if (minutesLeft < 10) {
        "0$minutesLeft"
    } else {
        minutesLeft.toString()
    }
    val seconds = if (secondsLeft < 10) {
        "0$secondsLeft"
    } else {
        secondsLeft.toString()
    }

    return minutes to seconds
}