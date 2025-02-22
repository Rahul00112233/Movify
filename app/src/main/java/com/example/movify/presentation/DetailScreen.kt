package com.example.movify.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.movify.api.MoviesDataById
import com.example.movify.viewmodel.TVShowViewModel

@SuppressLint("DefaultLocale")
@Composable
fun DetailScreen(viewModel: TVShowViewModel, navController: NavController) {
    val movie by viewModel.tvShowListById.observeAsState()
    val context = LocalContext.current

    // State to track whether the trailer is playing or not
    var isTrailerPlaying by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        // Conditionally render the video or the image
        if (isTrailerPlaying && viewModel.youtubeKey != null) {
            // Show the video when the trailer is playing
            VideoCard(videoKey = viewModel.youtubeKey!!)
        } else {
            // Show the image when the trailer is not playing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .background(Color.DarkGray)
            ) {
                val imageUrl = "https://image.tmdb.org/t/p/w500${movie?.poster_path}"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Movie Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(top = 32.dp, start = 8.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.LightGray)
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Movie Title
        movie?.let {
            Text(
                text = it.title,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Year, Age Rating, Duration, Languages
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            movie?.release_date?.let {
                Text(
                    text = it.take(4),
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Text(text = "• ${movie?.runtime} min", color = Color.White.copy(alpha = 0.7f))
            Text(
                text = "• ${movie?.spoken_languages?.size} Languages",
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Watch Trailer Button
        Button(
            onClick = { isTrailerPlaying = true }, // Update state to show the video
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.Black)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Watch Trailer Now", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Genres
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            movie?.genres?.forEach { genre ->
                Text(text = "${genre.name} | ", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Movie Overview
        movie?.let {
            Text(
                text = it.overview,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Budget, Revenue, and Rating
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Budget: $${movie?.budget}", color = Color.White.copy(alpha = 0.7f))
            Text(text = "Revenue: $${movie?.revenue}", color = Color.White.copy(alpha = 0.7f))
            Text(text = "Rating: ${movie?.vote_average}", color = Color.White.copy(alpha = 0.7f))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun VideoCard(videoKey: String) {
    val context = LocalContext.current

    // State to track full-screen mode
    var isFullScreen by remember { mutableStateOf(false) }

    // Full-screen view and custom view callback
    var fullScreenView by remember { mutableStateOf<View?>(null) }

    val videoUrl = """
        <iframe width="100%" height="100%" 
        src="https://www.youtube.com/embed/$videoKey?rel=0" 
      allowfullscreen></iframe>
    """.trimIndent()

    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        AndroidView(
            factory = {
                WebView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false

                    // Set WebChromeClient to handle full-screen mode
                    webChromeClient = object : WebChromeClient() {
                        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                            fullScreenView = view
                            isFullScreen = true
                        }

                        override fun onHideCustomView() {
                            fullScreenView = null
                            isFullScreen = false
                        }
                    }

                    loadData(videoUrl, "text/html", "utf-8")
                }
            },
            update = { webView ->
                webView.loadData(videoUrl, "text/html", "utf-8")
            }
        )
    }

    // Handle full-screen mode
    if (isFullScreen && fullScreenView != null) {
        AndroidView(
            factory = { context ->
                FrameLayout(context).apply {
                    addView(fullScreenView)
                }
            }
        )
    }
}