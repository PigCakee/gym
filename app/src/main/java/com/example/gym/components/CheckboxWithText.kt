package com.example.gym.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
fun CheckboxWithText(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier.size(20.dp),
            checked = isChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = GymTheme.colors.primary,
                uncheckedColor = GymTheme.colors.uiBorder,
                checkmarkColor = Color.White,
            ),
            onCheckedChange = onCheckedChange
        )
        Text(
            text = text,
            style = GymTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                lineHeight = 22.5.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1B1B1B),
            )
        )
    }
}