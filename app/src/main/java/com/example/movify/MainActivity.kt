package com.example.movify

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movify.api.MovieData
import com.example.movify.navigation.Navigation
import com.example.movify.presentation.HomeScreen
import com.example.movify.ui.theme.MovifyTheme
import com.example.movify.viewmodel.TVShowViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovifyTheme {
                SetSystemBarColors()
                Navigation(viewModel())
            }
        }
    }
}
@Composable
fun SetSystemBarColors() {
    val view = LocalView.current
    val isDarkTheme = isSystemInDarkTheme()

    SideEffect {
        val window = (view.context as Activity).window

        // Set status bar color to dark (e.g., black)
        window.statusBarColor = Color.Black.toArgb()

        // Ensure the status bar icons are light (white)
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = false // Light icons (white)
        }
    }
}

