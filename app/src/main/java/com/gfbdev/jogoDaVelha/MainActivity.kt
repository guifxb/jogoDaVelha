package com.gfbdev.jogoDaVelha

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gfbdev.jogoDaVelha.presentation.navigation.AppNavigation
import com.gfbdev.jogoDaVelha.ui.theme.JogoDaVelhaTheme
import com.google.android.gms.ads.MobileAds

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)


        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
        
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        MobileAds.initialize(this)

        setContent {
            JogoDaVelhaTheme {
                AppNavigation()
            }
        }
    }
}
