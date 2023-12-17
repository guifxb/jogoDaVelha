package com.gfbdev.jogoDaVelha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gfbdev.jogoDaVelha.presentation.navigation.NavController
import com.gfbdev.jogoDaVelha.ui.theme.JogoDaVelhaTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(1),
            navigationBarStyle = SystemBarStyle.dark(1)
        )

        super.onCreate(savedInstanceState)
        setContent {
            JogoDaVelhaTheme {
                NavController()
            }
        }
        MobileAds.initialize(this)
    }
}