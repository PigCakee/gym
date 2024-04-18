package com.example.gym.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.ui.theme.GymTheme

@Composable
fun ExerciseBlock(
    exerciseUi: ExerciseUi,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Box {
            GymAsyncImage(
                model = exerciseUi.imageUrl,
                modifier = modifier,
            )
            Surface(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = exerciseUi.name,
                        style = GymTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GymTheme.colors.textPrimary
                        )
                    )
                    Text(
                        text = "Primary: ${exerciseUi.primaryMuscles} | Secondary: ${exerciseUi.secondaryMuscles}",
                        style = GymTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = GymTheme.colors.textPrimary
                        )
                    )
                }
            }
        }
    }
}