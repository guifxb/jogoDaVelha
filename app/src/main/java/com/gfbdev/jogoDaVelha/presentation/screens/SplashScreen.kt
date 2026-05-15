package com.gfbdev.jogoDaVelha.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gfbdev.jogoDaVelha.R
import com.gfbdev.jogoDaVelha.ui.theme.LogoBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_anim))
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        speed = 1.0f,
        iterations = 1
    )

    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(progress) {
        if (progress == 1f && !hasNavigated) {
            hasNavigated = true
            onAnimationFinished()
        }
    }

    LaunchedEffect(Unit) {
        delay(4000)
        if (!hasNavigated) {
            hasNavigated = true
            onAnimationFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LogoBlue),
        contentAlignment = Alignment.Center
    ) {
        if (composition != null) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(250.dp),
            )
        }
    }
}
