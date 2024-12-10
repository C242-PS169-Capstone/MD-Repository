package com.herehearteam.herehear.data.remote.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    val response: ArticleResponseData
)

data class ArticleResponseData(
    val results: List<ArticleResult>
)

data class ArticleResult(
    val id: String,
    val webTitle: String,
    val sectionName: String,
    val webPublicationDate: String,
    val webUrl: String
)