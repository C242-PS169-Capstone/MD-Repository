package com.herehearteam.herehear.data.remote.api

import com.herehearteam.herehear.data.model.JournalRequestDto
import com.herehearteam.herehear.data.remote.response.ResponseJournal
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface ApiService {

    @POST("Journals")
    suspend fun createJournal(
        @Body request: JournalRequestDto
    ): ResponseJournal
}