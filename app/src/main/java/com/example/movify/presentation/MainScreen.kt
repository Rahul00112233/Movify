package com.example.movify.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movify.R
import com.example.movify.api.Result
import com.example.movify.viewmodel.TVShowViewModel


@Composable
fun MainScreen(viewModel: TVShowViewModel, navController: NavController) {
    val movieDetails by viewModel.tvShowListById.observeAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val popularMovies by viewModel.tvShowList.collectAsState()
    val upcomingMovies by viewModel.upcomingMovies.collectAsState()
    val topRatedMovies by viewModel.topratedMovies.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Search Bar
        Spacer(modifier = Modifier.height(64.dp))
        Row(modifier = Modifier.padding(start = 24.dp), verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(R.drawable.movify),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Movify", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))
        SearchBar(
            navController = navController
        )

        // Scrollable Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { MovieSection(
                title = "Trending Now", movies = trendingMovies,
                navController = navController,
                viewModel = viewModel
            ) }
            item { MovieSection(
                title = "Upcoming Movies", movies = upcomingMovies,
                navController = navController,
                viewModel = viewModel
            ) }
            item { MovieSection(
                title = "Top-Rated Movies", movies = topRatedMovies,
                navController = navController,
                viewModel = viewModel
            ) }
            item { MovieSection(
                title = "Popular Movies", movies = popularMovies,
                navController = navController,
                viewModel = viewModel
            ) }

        }
    }
}

@Composable
fun SearchBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .clickable { navController.navigate("search") } 
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Search movies...", color = Color.Gray, fontSize = 16.sp)
        }
    }
}


@Composable
fun MovieSection(title: String, movies: List<Result>, navController: NavController,viewModel: TVShowViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {
            items(movies) { movie ->
                MovieCard(
                    movie,
                    onClick = {
                        navController.navigate("detail/${movie.id}")
                        viewModel.fetchMoviesById(movie.id)
                    }
                )
            }
        }
    }
}

@Composable
fun MovieCard(movie:Result, onClick:()->Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
    AsyncImage(
        model = imageUrl,
        contentDescription = "Poster of ${movie.title}",
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .padding(end = 8.dp)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}
