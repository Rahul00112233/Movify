package com.example.movify.api

data class MoviesDataById(
    val adult: String,
    val backdrop_path: String,
    val budget: String,
    val genres: List<Genre>,
    val homepage: String,
    val id: String,
    val imdb_id: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: String,
    val poster_path: String,
    val release_date: String,
    val revenue: String,
    val runtime: String,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: String,
    val vote_average: Double,
    val vote_count: String
)