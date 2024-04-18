package com.example.gym.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.ui.theme.GymTheme

@Composable
fun ExerciseListItem(
    exerciseUi: ExerciseUi,
    onClick: (ExerciseUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick(exerciseUi) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        GymAsyncImage(
            model = exerciseUi.imageUrl,
            modifier = Modifier.size(24.dp),
            shape = CircleShape
        )
        Text(
            text = exerciseUi.name, style = GymTheme.typography.bodyMedium.copy(
                color = GymTheme.colors.textPrimary,
                fontSize = 14.sp
            )
        )
    }
}

data class ExerciseUi(
    val id: String,
    val imageUrl: String,
    val name: String,
    val primaryMuscles: List<String>,
    val secondaryMuscles: List<String>
)