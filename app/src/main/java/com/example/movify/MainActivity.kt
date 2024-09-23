package com.example.movify

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movify.api.MovieData
import com.example.movify.navigation.Navigation
import com.example.movify.presentation.HomeScreen
import com.example.movify.ui.theme.MovifyTheme
import com.example.movify.viewmodel.TVShowViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val api = "9f89e68fa800f88bab3d8e0af8b5df73"

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovifyTheme {
                Navigation(viewModel = viewModel())
            }
        }
    }
}
