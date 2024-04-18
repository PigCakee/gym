package com.example.gym.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gym.ui.theme.Mono600
import com.example.gym.ui.theme.Success

@Composable
fun GymPlus(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        border = BorderStroke(1.dp, Mono600),
        shape = CircleShape,
        color = Success,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(modifier = modifier) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
            )
        }
    }
}

@Composable
fun GymFinish(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        border = BorderStroke(1.dp, Mono600),
        shape = CircleShape,
        color = Success,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(modifier = modifier) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
            )
        }
    }
}