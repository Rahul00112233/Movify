package com.example.movify.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movify.api.Result
import com.example.movify.ui.theme.BlueCustom
import com.example.movify.viewmodel.TVShowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: TVShowViewModel, navController: NavHostController){
    var query by remember { mutableStateOf(value = "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it},
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = {
                            Text(text = "Search for movies")
                        },
                        shape = RoundedCornerShape(35.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, colors = IconButtonDefaults.iconButtonColors(BlueCustom)) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchSearchedMovies(query) }, colors = IconButtonDefaults.iconButtonColors(BlueCustom)){
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
                        }
                    })
        }
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            val movieSearches by viewModel.tvShowListBySearch.collectAsState()
            Text(text = "Search Results", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(movieSearches){movieShow->
                    MovieSearchCard(movie = movieShow, onClick={
                        navController.navigate("detail/${movieShow.id}")
                        viewModel.fetchMoviesById(movieShow.id)
                    })
                }
            }
        }
    }
}

@Composable
fun MovieSearchCard(movie: Result, onClick: ()->Unit){
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(12.dp)
    ){
        AsyncImage(
            model = imageUrl,
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(8.dp)){
            movie.title?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            movie.release_date?.let { Text(text = it, fontSize = 10.sp) }
        }
    }
}

