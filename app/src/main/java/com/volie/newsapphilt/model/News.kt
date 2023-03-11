package com.volie.newsapphilt.model


data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)