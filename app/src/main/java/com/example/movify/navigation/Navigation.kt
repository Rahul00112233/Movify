package com.example.movify.navigation

import MovieSearchScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movify.presentation.DetailScreen
import com.example.movify.presentation.MainScreen
import com.example.movify.presentation.StartScreen
import com.example.movify.viewmodel.TVShowViewModel

@Composable
fun Navigation(viewModel: TVShowViewModel){
    val navController: NavHostController  = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        composable(route = "start"){
            StartScreen(navController)
        }
        composable(route = "home"){
            MainScreen(viewModel = viewModel, navController = navController)
        }
//        composable(route = "detail/{id}"){
//            MovieScreenUI(viewModel, navController)
//        }
        composable(route = "search"){
            MovieSearchScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = "detail/{id}"){
            DetailScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

    }
}