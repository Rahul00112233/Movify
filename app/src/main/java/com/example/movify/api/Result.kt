package com.example.movify.api

data class Result(
    val adult: String?,
    val backdrop_path: String?,
    val genre_ids: List<String>?,
    val id: Int,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: String?,
    val vote_average: String?,
    val vote_count: String?
)