package com.example.gym.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.ui.theme.Gradient
import com.example.gym.ui.theme.GymTheme

@Composable
fun Switcher(
    leftText: String,
    rightText: String,
    modifier: Modifier = Modifier,
    state: SwitchState,
    onSwitch: (SwitchState) -> Unit
) {
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = Color.Transparent,
        border = BorderStroke(1.dp, color = GymTheme.colors.uiBorder)
    ) {
        val leftBackground = if (state is SwitchState.Left) {
            Modifier.background(
                shape = RoundedCornerShape(6.dp),
                brush = Gradient
            )
        } else {
            Modifier.background(
                shape = RoundedCornerShape(6.dp),
                color = Color.Transparent
            )
        }
        val rightBackground = if (state is SwitchState.Right) {
            Modifier.background(
                shape = RoundedCornerShape(6.dp),
                brush = Gradient
            )
        } else {
            Modifier.background(
                shape = RoundedCornerShape(6.dp),
                color = Color.Transparent
            )
        }
        Row(
            modifier = modifier.height(34.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(3.dp)
                    .then(leftBackground)
                    .clickable { onSwitch(SwitchState.Left) }
                    .padding(4.dp),
                text = leftText,
                style = GymTheme.typography.bodyMedium.copy(
                    lineHeight = 20.3.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (state is SwitchState.Left) Color.White else GymTheme.colors.textPrimary,
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(3.dp)
                    .then(rightBackground)
                    .clickable { onSwitch(SwitchState.Right) }
                    .padding(4.dp),
                text = rightText,
                style = GymTheme.typography.bodyMedium.copy(
                    lineHeight = 20.3.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (state is SwitchState.Right) Color.White else GymTheme.colors.textPrimary,
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewSwitcher() {
    GymTheme {
        Column {
            Switcher(
                modifier = Modifier.size(width = 328.dp, height = 34.dp),
                leftText = "Left",
                rightText = "Right",
                state = SwitchState.Left,
                onSwitch = {}
            )
            Spacer(modifier = Modifier.height(20.dp))
            Switcher(
                modifier = Modifier.size(width = 328.dp, height = 34.dp),
                leftText = "Left",
                rightText = "Right",
                state = SwitchState.Right,
                onSwitch = {}
            )
        }
    }
}

sealed class SwitchState {
    data object Left : SwitchState()
    data object Right : SwitchState()
}