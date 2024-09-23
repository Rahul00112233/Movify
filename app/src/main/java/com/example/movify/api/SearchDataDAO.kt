package com.example.movify.api

data class SearchDataDAO(
    val page: Int,
    val results: List<ResultXX>,
    val total_pages: Int,
    val total_results: Int
)