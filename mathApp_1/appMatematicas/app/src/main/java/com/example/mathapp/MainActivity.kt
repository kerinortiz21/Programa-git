package com.example.mathapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mathapp.ui.theme.MathAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Compose will control the syste background
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MathAppTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = Color.White.luminance() > 0.5 // fondo blanco
                val backgroundColor = Color.White

                //  bar system transparents
                SideEffect {
                    // status bar transparent
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                    // navegation bar transaprent
                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }

                val navController = rememberNavController()
                val viewModel: AuthViewModel = viewModel()

                // background white
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .systemBarsPadding() // superpotition of bars
                ) {
                    AppNavigation(navController, viewModel)
                }
            }
        }
    }
}
