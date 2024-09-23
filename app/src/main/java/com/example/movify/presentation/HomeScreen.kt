package com.example.movify.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movify.viewmodel.TVShowViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movify.R
import com.example.movify.api.Result


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: TVShowViewModel = viewModel(), navController: NavController){

    val movieShows by viewModel.tvShowList.collectAsState()
    val trendingShows by viewModel.trendingMovies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                                 Image(
                                     painter = painterResource(id = R.drawable.movielogo),
                                     contentDescription = "logo",
                                     modifier = Modifier
                                         .size(50.dp)
                                         .padding(5.dp),
                                 )
                },
                title = { 
                        Text(text = "MOVIFY", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(26.dp)
                        )
                    }

                }
            )
        }
    ){PaddingValues->
        Column(
            modifier = Modifier
                .padding(PaddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Popular Movies",
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(movieShows) { movieShow ->
                    MovieCard(
                        movie = movieShow,
                        onClick = {
                            navController.navigate("detail/${movieShow.id}")
                            viewModel.fetchMoviesById(movieShow.id)
                        }
                    )
                }
            }

            Text(
                text = "Trending Movies",
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(trendingShows) { trendingShow ->
                    MovieCard(
                        movie = trendingShow,
                        onClick = {
                            navController.navigate("detail/${trendingShow.id}")
                            viewModel.fetchMoviesById(trendingShow.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: Result, onClick: ()->Unit){
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
            .clickable {
                onClick()
            }
            .width(150.dp),
        shape = RoundedCornerShape(12.dp)
    ){
        AsyncImage(
            model = imageUrl,
            contentDescription = "",
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
            Text(text = movie.release_date!!, fontSize = 10.sp)
        }
    }
}


