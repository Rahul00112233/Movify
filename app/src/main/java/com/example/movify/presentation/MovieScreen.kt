package com.example.movify.presentation

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movify.R
import com.example.movify.api.MoviesDataById
import com.example.movify.ui.theme.Blue
import com.example.movify.ui.theme.Inside
import com.example.movify.viewmodel.TVShowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreenUI(viewModel: TVShowViewModel, navController: NavHostController) {

    val movieDetails by viewModel.tvShowListById.observeAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Movie Details", style = MaterialTheme.typography.bodyLarge)
                },
                navigationIcon = {
                    IconButton(onClick = { }, colors = IconButtonDefaults.iconButtonColors(
                        Inside)) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            viewModel.youtubeKey?.let { VideoCard(videoKey = it, context = context) }

            movieDetails?.let { MovieIntroCard(moviesDataById = it) }

            movieDetails?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RatingCard(moviesDataById = it)
                    BudgetCard(moviesDataById = it)
                }
            }
        }
    }
}

@Composable
fun VideoCard(videoKey: String, context: Context) {
    val videoUrl = """
        <iframe width="100%" height="300" 
        src="https://www.youtube.com/embed/$videoKey?rel=0" 
        frameborder="0" allowfullscreen></iframe>
    """.trimIndent()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        AndroidView(factory = {
            WebView(context).apply {
                clearCache(true)
                clearHistory()
                settings.javaScriptEnabled = true
                WebChromeClient()
                loadData(videoUrl, "text/html", "utf-8")
            }
        },
            update = {webview->
                webview.loadData(videoUrl,"text/html", "utf-8")

            }
        )
    }
}

@Composable
fun MovieIntroCard(moviesDataById: MoviesDataById) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${moviesDataById.poster_path}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = moviesDataById.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = moviesDataById.tagline,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${moviesDataById.release_date} • ${moviesDataById.runtime} min • ${moviesDataById.original_language?.uppercase()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = moviesDataById.overview,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun RatingCard(moviesDataById: MoviesDataById) {
    val rating = moviesDataById.vote_average
    val ratingColor = when {
        rating >= 7 -> Color(0xFF4CAF50)
        rating >= 5 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Card(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Rating Star",
                tint = ratingColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = String.format("%.1f", rating),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ratingColor
            )
        }
    }
}

@Composable
fun BudgetCard(moviesDataById: MoviesDataById) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Status", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text(text = moviesDataById.status, style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Budget", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${moviesDataById.budget}", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Revenue", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${moviesDataById.revenue}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}