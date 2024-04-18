package com.example.gym.nav

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavigationItem(
    val label : String,
    val icon : Painter,
    val destination : String
)
