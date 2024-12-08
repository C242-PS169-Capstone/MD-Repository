package com.herehearteam.herehear.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("model1") val model1: ModelPrediction? = null,
    @SerializedName("model2") val model2: ModelPrediction? = null
)

data class ModelPrediction(
    @SerializedName("prediction") val prediction: String,
    @SerializedName("confidence") val confidence: String
)

// Error response for API errors
data class ErrorResponse(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String? = null
)
