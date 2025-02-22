
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movify.R
import com.example.movify.api.Result
import com.example.movify.viewmodel.TVShowViewModel

@Composable
fun MovieSearchScreen(navController: NavController, viewModel:TVShowViewModel) {
    var query by remember { mutableStateOf("") }
    val movies by viewModel.tvShowListBySearch.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()

    val filteredMovies = movies.filter { movie ->
        !movie.poster_path.isNullOrEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 64.dp)
    ) {
        Row(modifier = Modifier.padding(start = 24.dp), verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(R.drawable.movify),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Movify", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.searchMovies(it)
                },
                placeholder = { Text("Search movies...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.LightGray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    focusedTextColor  = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (query == "") {
            Text(
                text = "Trending Movies",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(all = 8.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(trendingMovies) { movie ->
                    MovieItem(
                        movie,
                        onClick = {
                            navController.navigate("detail/${movie.id}")
                            viewModel.fetchMoviesById(movie.id)
                        }
                    )
                }
            }

        } else {
            Text(
                text = "Top Results",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(all = 8.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredMovies) { movie ->
                    MovieItem(
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
}

@SuppressLint("DefaultLocale")
@Composable
fun MovieItem(movie: Result, modifier: Modifier = Modifier, onClick:()->Unit) {
    Column(
        modifier = modifier
            .width(140.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Movie Poster with Async Image Loading
        Image(
            painter = rememberAsyncImagePainter(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                placeholder = painterResource(R.drawable.movify),
            ),
            contentDescription = movie.title,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Movie Title
        movie.title?.let {
            Text(
                text = it,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Movie Rating
        val roundedRating = movie.vote_average?.toDoubleOrNull()?.let { String.format("%.1f", it) } ?: "0.0"

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = Color.Yellow,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = " $roundedRating",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
