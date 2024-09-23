package com.example.movify.api

data class MovieData(
    val page: String,
    val results: List<Result>,
    val total_pages: String,
    val total_results: String
)