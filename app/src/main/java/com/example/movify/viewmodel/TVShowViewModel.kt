package com.example.movify.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.movify.api.MovieData
import com.example.movify.api.MoviesDataById
import com.example.movify.api.Result
import com.example.movify.api.ResultX
import com.example.movify.api.VideosDataById
import com.example.movify.api.apiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TVShowViewModel : ViewModel() {

    private val _tvShowList = MutableStateFlow<List<Result>>(emptyList())
    val tvShowList: StateFlow<List<Result>> = _tvShowList

    private val _tvShowListById = MutableLiveData<MoviesDataById>()
    val tvShowListById: LiveData<MoviesDataById> = _tvShowListById

    private val _tvShowListBySearch = MutableStateFlow<List<Result>>(emptyList())
    val tvShowListBySearch: StateFlow<List<Result>> = _tvShowListBySearch

    private val _trendingMovies = MutableStateFlow<List<Result>>(emptyList())
    val trendingMovies : StateFlow<List<Result>> = _trendingMovies

    private val _upcomingMovies = MutableStateFlow<List<Result>>(emptyList())
    val upcomingMovies : StateFlow<List<Result>> = _upcomingMovies

    private val _topratedMovies = MutableStateFlow<List<Result>>(emptyList())
    val topratedMovies : StateFlow<List<Result>> = _topratedMovies

    private val _query = mutableStateOf("")
    val query: State<String> = _query

    var youtubeKey by mutableStateOf<String?>(null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        fetchPopularMovies()
        fetchTrendingMovies()
        fetchUpcomingMovies()
        fetchTopRatedMovies()
    }
    fun searchMovies(query: String) {
        _query.value = query
        viewModelScope.launch {
            try {
                val response = apiService.getSearchedMovies(query = query)
                if (response.isSuccessful) {
                    _tvShowListBySearch.value = response.body()?.results ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("MovieSearch", "Error fetching movies: ${e.message}")
            }
        }
    }

//    fun fetchSearchedMovies(Query:String){
//        viewModelScope.launch(Dispatchers.IO) {
//            try{
//                val response = apiService.getSearchedMovies(query = Query)
//                if(response.isSuccessful){
//                    val movieData = response.body()
//                    movieData?.let {
//                        _tvShowListBySearch.value = response.body()?.results?:emptyList()
//                    }
//                } else{
//                    Log.e("TVShowViewModel", "Error fetching discovered movies: ${response.message()}")
//                }
//            } catch (e: Exception){
//
//            }finally {
//                _isLoading.value = false
//            }
//        }
//    }
    fun fetchPopularMovies() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = apiService.getDiscoveredMovies()
                if(response.isSuccessful){
                    val movieData = response.body()
                    movieData?.let{
                        _tvShowList.value = response.body()?.results?:emptyList()
                    }
                }
            } catch (e: Exception){

            }finally {
                _isLoading.value = false
            }
        }
    }
    fun fetchTrendingMovies(){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = apiService.getTrendingMovies()
                if(response.isSuccessful){
                    val movieData = response.body()
                    movieData?.let {
                        _trendingMovies.value = response.body()?.results?:emptyList()
                    }
                }
            } catch (e: Exception){

            }finally {
                _isLoading.value = false
            }

        }
    }
    fun fetchUpcomingMovies(){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = apiService.getUpcomingMovies()
                if(response.isSuccessful){
                    val movieData = response.body()
                    movieData?.let {
                        _upcomingMovies.value = response.body()?.results?:emptyList()
                    }
                    Log.d("Responseit", response.toString())
                }
            } catch (e: Exception){
                Log.d("Responseit", "not fetching data")
            }finally {
                _isLoading.value = false
            }

        }
    }
    fun fetchTopRatedMovies(){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = apiService.getTopRatedMovies()
                if(response.isSuccessful){
                    val movieData = response.body()
                    movieData?.let {
                        _topratedMovies.value = response.body()?.results?:emptyList()
                    }
                    Log.d("Responseit", response.toString())
                }
            } catch (e: Exception){
                Log.d("Responseit", "not fetching data")
            }finally {
                _isLoading.value = false
            }

        }
    }
    fun fetchMoviesById(id: Int){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = apiService.getMovies(movieId = id)
                val videoResponse = apiService.getVideo(movieId = id)
                if(videoResponse.isSuccessful){
                    val videoData = videoResponse.body()
                    val trailer = videoData?.results?.firstOrNull{
                        it.site == "YouTube" && it.type == "Trailer"
                    }
                    youtubeKey = trailer?.key
                }
                if(response.isSuccessful) {
                    val movieData = response.body()
                    movieData?.let {
                        _tvShowListById.postValue(it)
                    }
                }
                else{
                    Log.e("TVShowViewModel", "Error fetching movies by ID: ${response.message()}")
                }
            }catch (e: Exception){
                Log.e("TVShowViewModel", "Error fetching movies by ID: ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }

}