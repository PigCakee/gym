package com.example.gym.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimatedProgressBar(
    modifier: Modifier = Modifier,
    level: Int,
    maxLevel: Int,
    color: Color? = null,
    brush: Brush? = null,
    thickness: Dp = 4.dp
) {
    val animateValue = remember { Animatable(0f) }
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(progress) {
        if (level > 0f) {
            animateValue.snapTo(0f)
            delay(10)
            animateValue.animateTo(
                targetValue = progress,
                animationSpec = tween(
                    durationMillis = 1000
                )
            )
        }
    }

    androidx.compose.foundation.Canvas(
        modifier = modifier,
        onDraw = {
            progress = size.width / maxLevel * level
            drawLine(
                color = Color(0xFFE8EFF6),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                cap = StrokeCap.Round,
                strokeWidth = thickness.toPx(),
            )
            if (color != null) {
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(animateValue.value, 0f),
                    cap = StrokeCap.Round,
                    strokeWidth = thickness.toPx(),
                )
            }
            if (brush != null) {
                drawLine(
                    brush = brush,
                    start = Offset(0f, 0f),
                    end = Offset(animateValue.value, 0f),
                    cap = StrokeCap.Round,
                    strokeWidth = thickness.toPx(),
                )
            }
        }
    )
}