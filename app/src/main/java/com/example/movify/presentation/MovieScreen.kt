package com.example.movify.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movify.R
import com.example.movify.api.MoviesDataById
import com.example.movify.ui.theme.BlueCustom
import com.example.movify.viewmodel.TVShowViewModel
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreenUI(viewModel: TVShowViewModel, navController: NavHostController){

    val movieDetails by viewModel.tvShowListById.observeAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, colors = IconButtonDefaults.iconButtonColors(BlueCustom)) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            viewModel.youtubeKey?.let { VideoCard(videoKey = it, context = context) }
            movieDetails?.let { MovieIntroCard(moviesDataById = it, navController = navController) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                movieDetails?.let { RatingCard(moviesDataById = it) }
                movieDetails?.let { BudgetCard(moviesDataById = it) }
            }
        }
    }
}

@Composable
fun VideoCard(videoKey: String, context: Context){
        val videoUrl = "<iframe width=\"100%\" height=\"600\" src=\"https://www.youtube.com/embed/${videoKey}?rel=0\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
        AndroidView(factory = {
            WebView(context).apply {
                clearCache(true)
                clearHistory()
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                WebChromeClient()
                loadData(videoUrl, "text/html", "utf-8")
            }
        },
            update = {webview->
                webview.loadData(videoUrl,"text/html", "utf-8")

            }
        )
}

@Composable
fun MovieIntroCard(moviesDataById: MoviesDataById, navController: NavController){
    //val imageUrl = "https://image.tmdb.org/t/p/w200${moviesDataById.poster_path}"
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp), colors = CardDefaults.cardColors(BlueCustom),
        shape = RectangleShape
    ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = moviesDataById.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = moviesDataById.tagline,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp
                )
                Text(text = moviesDataById.release_date + " • " + moviesDataById.runtime + "min" + " • " + moviesDataById.original_language)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = moviesDataById.overview)
            }


    }
}
@SuppressLint("DefaultLocale")
@Composable
fun RatingCard(moviesDataById: MoviesDataById){
    Card(colors = CardDefaults.cardColors(BlueCustom), modifier = Modifier.padding(start = 4.dp)){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ){
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "",
                Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = String.format("%.1f", moviesDataById.vote_average), fontSize = 22.sp)
        }

    }
}

@Composable
fun BudgetCard(moviesDataById: MoviesDataById){
    Card(modifier = Modifier.padding(12.dp),){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Status", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = moviesDataById.status)
            }
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Budget", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "$"+moviesDataById.budget)
            }
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Revenue", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "$"+moviesDataById.revenue)
            }
        }
    }
}