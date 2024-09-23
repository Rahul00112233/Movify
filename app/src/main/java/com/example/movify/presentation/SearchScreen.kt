package com.example.movify.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movify.api.Result
import com.example.movify.ui.theme.Blue
import com.example.movify.ui.theme.Inside
import com.example.movify.viewmodel.TVShowViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: TVShowViewModel, navController: NavHostController) {
    var query by remember { mutableStateOf(value = "") }

    val movieSearches by viewModel.tvShowListBySearch.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        placeholder = {
                            Text(text = "Search for movies", color = Color.Gray)
                        },
                        shape = RoundedCornerShape(35.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Inside,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Inside
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.fetchSearchedMovies(query)
                                keyboardController?.hide()
                            }
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}, colors = IconButtonDefaults.iconButtonColors(
                        Inside)) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.fetchSearchedMovies(query)
                    }, colors = IconButtonDefaults.iconButtonColors(Inside)) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                },

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (!isLoading && movieSearches.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    items(movieSearches) { movieShow ->
                        MovieSearchCard(movie = movieShow, onClick = {
                            navController.navigate("detail/${movieShow.id}")
                            viewModel.fetchMoviesById(movieShow.id)
                        })
                    }
                }
            } else if (!isLoading) {
                Text(
                    text = "No results found.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun MovieSearchCard(movie: Result, onClick: () -> Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
    Card(
        modifier = Modifier
            .padding(6.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(12.dp)
    ) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent)))
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
            ) {
                movie.title?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                movie.release_date?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
