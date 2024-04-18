package com.example.gym.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.R
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.White

@Composable
fun ButtonNextScreen(
    modifier: Modifier = Modifier,
    title: String,
    painter: Painter,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color(0xFFEAEFF8),
                shape = RoundedCornerShape(size = 8.dp)
            ),
        color = White,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painter,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = GymTheme.typography.titleSmall.copy(
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1B1B1B),
                )
            )
            Image(
                painter = painterResource(id = R.drawable.ic_chevron_right_mono500_24dp),
                contentDescription = null
            )
        }
    }
}