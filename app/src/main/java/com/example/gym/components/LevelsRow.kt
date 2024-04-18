package com.example.gym.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.ui.theme.GymTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LevelsRow(
    levels: List<Int>,
    selectedLevel: Int,
    onLevelClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        levels.forEachIndexed { index, level ->
            Surface(
                contentColor = Color.White,
                color = if (index == levels.indexOf(selectedLevel)) Color(0xFFE4ECF3) else Color.White,
                border = BorderStroke(1.dp, Color(0xFFEAEFF8)),
                shape = RoundedCornerShape(2000.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(height = 34.dp, width = 48.dp)
                        .clickable { onLevelClick(level) },
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = level.toString(),
                        style = GymTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            lineHeight = 18.2.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF000000),
                            textAlign = TextAlign.Center,
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}