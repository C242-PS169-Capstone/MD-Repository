package com.herehearteam.herehear.data.local.repository

import com.herehearteam.herehear.data.model.PredictionRequest
import com.herehearteam.herehear.data.remote.api.ApiService
import com.herehearteam.herehear.data.remote.response.PredictionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PredictionRepository(private val apiService: ApiService) {
    suspend fun predictText(text: String, model: String = "both"): Result<PredictionResponse> {
        return try {
            withContext(Dispatchers.IO) {
                val request = PredictionRequest(text, model)
                val response = apiService.predictText(request)
                Result.success(response)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}