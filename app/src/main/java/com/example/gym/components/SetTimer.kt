package com.example.gym.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.ui.theme.GymTheme
import kotlin.math.floor

@Composable
fun SetTimer(
    millis: Long,
    onStart: () -> Unit,
) {
    var isStarted by remember {
        mutableStateOf(false)
    }
    val time = getMinutesAndSeconds(millis)
    if (isStarted) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    shape = CircleShape,
                    color = GymTheme.colors.primary
                ),
        ) {
            Text(
                text = "${time.first}:${time.second}",
                modifier = Modifier.align(Alignment.Center),
                style = GymTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            )
        }
    } else {
        FloatingActionButton(
            modifier = Modifier.size(64.dp),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
                pressedElevation = 0.dp
            ),
            onClick = {
                onStart()
                isStarted = true
            },
            shape = CircleShape,
            containerColor = GymTheme.colors.primary
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White
            )
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