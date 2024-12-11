package com.herehearteam.herehear.data.remote.api

import com.herehearteam.herehear.data.model.EmergencyContactRequest
import com.herehearteam.herehear.data.model.EmergencyContactUpdateRequest
import com.herehearteam.herehear.data.model.JournalRequestDto
import com.herehearteam.herehear.data.model.PredictionRequest
import com.herehearteam.herehear.data.model.UserRequestDto
import com.herehearteam.herehear.data.remote.response.AllUsersResponse
import com.herehearteam.herehear.data.remote.response.ArticleResponse
import com.herehearteam.herehear.data.remote.response.EmergencyContactResponse
import com.herehearteam.herehear.data.remote.response.PredictionResponse
import com.herehearteam.herehear.data.remote.response.ResponseAllJournal
import com.herehearteam.herehear.data.remote.response.ResponseJournal
import com.herehearteam.herehear.data.remote.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("Journals")
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

    @POST("emergency-contacts")
    suspend fun createEmergencyContact(
        @Body request: EmergencyContactRequest
    ): EmergencyContactResponse

    @GET("emergency-contacts")
    suspend fun getAllEmergencyContacts(
    ): EmergencyContactResponse

    @PUT("emergency-contacts/{id}")
    suspend fun updateEmergencyContact(
        @Path("id") id: Int,
        @Body request: EmergencyContactUpdateRequest
    ): EmergencyContactResponse

    @GET("search")
    suspend fun searchArticles(
        @Query("q") query: String,
        @Query("api-key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("page-size") pageSize: Int = 10
    ): ArticleResponse

    @POST("users")
    suspend fun createUser(
        @Body request: UserRequestDto
    ): UserResponse

    @GET("users")
    suspend fun getAllUsers(): AllUsersResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") userId: String
    ): UserResponse
}