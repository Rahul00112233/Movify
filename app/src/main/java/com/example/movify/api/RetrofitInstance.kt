package com.example.movify.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val client = OkHttpClient()

val apiKeyInterceptor = Interceptor { chain ->
    val originalRequest: Request = chain.request()
    val newRequest = originalRequest.newBuilder()
        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5Zjg5ZTY4ZmE4MDBmODhiYWIzZDhlMGFmOGI1ZGY3MyIsIm5iZiI6MTcyNTM2MjI1MC4xNjM3NDYsInN1YiI6IjY2ZDZlOGMzMWY1ZjZhNzY1OGRkNTU2MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.W0uMSXy2o0VaMbr32gq2iULP4fi7xgI_an56_MliAW0")
        .build()
    chain.proceed(newRequest)
}

// OkHttpClient with the Interceptor
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(apiKeyInterceptor)
    .build()

interface RetrofitInstance {
    @GET("search/movie")
    suspend fun getSearchedMovies(
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("query") query: String
    ): Response<MovieData>

    @GET("discover/movie")
    suspend fun getDiscoveredMovies(
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String= "popularity.desc",
        @Query("page") page: Int = 1
    ): Response<MovieData>

    @GET("movie/{movies_id}")
    suspend fun getMovies(
        @Path("movies_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<MoviesDataById>

    @GET("movie/{movies_id}/videos")
    suspend fun getVideo(
        @Path("movies_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<VideosDataById>

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("language") language: String = "en-US"
    ): Response<MovieData>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = "en-US"
    ): Response<MovieData>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "en-US"
    ): Response<MovieData>
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.themoviedb.org/3/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService : RetrofitInstance = retrofit.create(RetrofitInstance::class.java)