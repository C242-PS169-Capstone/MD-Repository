package com.herehearteam.herehear.data.remote.api

import com.herehearteam.herehear.data.model.JournalRequestDto
import com.herehearteam.herehear.data.model.PredictionRequest
import com.herehearteam.herehear.data.remote.response.ArticleResponse
import com.herehearteam.herehear.data.remote.response.PredictionResponse
import com.herehearteam.herehear.data.remote.response.ResponseAllJournal
import com.herehearteam.herehear.data.remote.response.ResponseJournal
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("Journals/")
    suspend fun createJournal(
        @Body request: JournalRequestDto
    ): ResponseJournal

    @GET("Journals")
    suspend fun getAllJournals(
        @Query("user_id") userId: String
    ): ResponseAllJournal

    @POST("predict")
    suspend fun predictText(
        @Body request: PredictionRequest
    ): PredictionResponse

    @GET("search")
    suspend fun searchArticles(
        @Query("q") query: String,
        @Query("api-key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("page-size") pageSize: Int = 10
    ): ArticleResponse
}