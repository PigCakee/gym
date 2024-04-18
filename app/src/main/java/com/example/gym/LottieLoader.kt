package com.example.gym

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieLoader(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    @RawRes animRes: Int = R.raw.lottie_loader,
) {
    if (isVisible) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animRes))
        LottieAnimation(
            modifier = modifier,
            composition = composition
        )
    }
}