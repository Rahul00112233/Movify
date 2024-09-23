package com.example.movify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movify.presentation.HomeScreen
import com.example.movify.presentation.MovieScreenUI
import com.example.movify.presentation.SearchScreen
import com.example.movify.viewmodel.TVShowViewModel

@Composable
fun Navigation(viewModel: TVShowViewModel){
    val navController: NavHostController  = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home"){
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = "detail/{id}"){
            MovieScreenUI(viewModel, navController)
        }
        composable(route = "search"){
            SearchScreen(viewModel = viewModel, navController = navController)
        }

    }
}