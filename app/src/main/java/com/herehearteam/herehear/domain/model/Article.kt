package com.herehearteam.herehear.domain.model

data class Article(
    val id: String,
    val title: String,
    val tags: List<String>,
    val date: String,
    val imageRes: Int,
    val url: String
)