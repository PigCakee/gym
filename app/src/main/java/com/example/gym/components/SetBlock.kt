package com.example.gym.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.data.Set
import com.example.gym.ui.theme.GymTheme
import kotlin.math.floor

@Composable
fun SetBlock(
    set: Set,
    setNum: Int,
    onModify: (Set) -> Unit,
    millis: Long,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.Black),
        color = Color.White
    ) {
        Row(
            modifier = modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        shape = CircleShape,
                        color = GymTheme.colors.primary
                    ),
            ) {
                Text(
                    text = setNum.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    style = GymTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                WheelPicker(
                    modifier = Modifier.height(82.dp),
                    range = (0..200).toList(),
                    suggestedSelection = floor(set.sugWeight).toInt(),
                    width = tripleDigitsWidth,
                    onSelectionChanged = {
                        onModify(
                            set.copy(
                                actualWeight = it.toDouble() + (((set.actualWeight
                                    ?: set.sugWeight) * 1000) % 1000)
                            )
                        )
                    }
                )
                Text(
                    text = ".",
                    style = GymTheme.typography.bodyMedium.copy(
                        fontSize = 18.sp,
                        color = GymTheme.colors.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                WheelPicker(
                    modifier = Modifier.height(82.dp),
                    range = List(8) { index -> index * 125 },
                    suggestedSelection = ((set.sugWeight * 1000) % 1000).toInt(),
                    width = tripleDigitsWidth,
                    onSelectionChanged = {
                        onModify(
                            set.copy(
                                actualWeight = it.toDouble() + ((set.actualWeight
                                    ?: set.sugWeight) / 1000)
                            )
                        )
                    }
                )
                Text(
                    text = "kg ",
                    style = GymTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        color = GymTheme.colors.primary,
                        fontWeight = FontWeight.Medium
                    )
                )
                WheelPicker(
                    modifier = Modifier.height(82.dp),
                    range = (0..20).toList(),
                    suggestedSelection = set.sugReps.toInt(),
                    width = doubleDigitsWidth,
                    onSelectionChanged = {
                        onModify(set.copy(actualReps = it.toDouble()))
                    }
                )
                Text(
                    text = "reps",
                    style = GymTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        color = GymTheme.colors.primary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            SetTimer(millis = millis, onStart = onStart)
        }
    }
}