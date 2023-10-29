package com.gfbdev.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gfbdev.myapplication.presentation.navigation.NavController
import com.gfbdev.myapplication.ui.theme.JogoDaVelhaTheme

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

    }
}


