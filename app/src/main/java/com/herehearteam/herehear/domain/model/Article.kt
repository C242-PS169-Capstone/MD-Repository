package com.herehearteam.herehear.domain.model

data class Article(
    val id: String,
    val title: String,
    val sectionName: String,
    val publicationDate: String,
    val webUrl: String,
    val description: String,
    val imageRes: Int,
)