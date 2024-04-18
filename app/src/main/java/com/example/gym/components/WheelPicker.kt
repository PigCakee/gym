package com.example.gym.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.ui.theme.GymTheme
import kotlinx.coroutines.delay

val doubleDigitsWidth = 24.dp
val tripleDigitsWidth = 32.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelPicker(
    modifier: Modifier = Modifier,
    range: List<Int>,
    width: Dp,
    suggestedSelection: Int,
    onSelectionChanged: (Int) -> Unit
) {
    val state = rememberLazyListState()
    val list = listOf(-1) + range + listOf(-1)
    var selection by remember {
        mutableIntStateOf(suggestedSelection)
    }
    val flingBehavior = rememberSnapFlingBehavior(state)
    LaunchedEffect(suggestedSelection) {
        state.scrollToItem(list.indexOf(suggestedSelection) - 1)
    }
    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress) {
            selection = list.elementAtOrNull(
                state.layoutInfo.visibleItemsInfo.elementAtOrNull(
                    state.layoutInfo.visibleItemsInfo.size / 2
                )?.index ?: 0
            ) ?: 0
            onSelectionChanged(selection)
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            HorizontalDivider(
                color = GymTheme.colors.primary,
                thickness = 2.dp,
                modifier = Modifier.width(32.dp)
            )
            HorizontalDivider(
                color = GymTheme.colors.primary,
                thickness = 2.dp,
                modifier = Modifier.width(32.dp)
            )
        }
        LazyColumn(
            modifier = Modifier
                .align(Alignment.Center)
                .width(width),
            state = state,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = flingBehavior
        ) {
            itemsIndexed(list) { index, item ->
                if (index == 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "$item",
                    style = GymTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = when {
                            item == -1 -> Color.Transparent
                            selection == item -> GymTheme.colors.primary
                            selection != item -> GymTheme.colors.textSecondary
                            else -> Color.Transparent
                        },
                        textAlign = TextAlign.Center
                    )
                )
                if (index == list.lastIndex) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        Box(
            modifier = modifier
                .width(width)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White,
                            Color.White,
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0f),
                            Color.White,
                            Color.White,
                        )
                    )
                )
        )
    }
}