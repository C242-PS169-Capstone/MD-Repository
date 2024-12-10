package com.herehearteam.herehear.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictionResponse(
    @SerializedName("model1") val model1: ModelPrediction? = null,
    @SerializedName("model2") val model2: ModelPrediction? = null
): Parcelable

@Parcelize
data class ModelPrediction(
    @SerializedName("prediction") val prediction: String,
    @SerializedName("confidence") val confidence: String
): Parcelable

// Error response for API errors
data class ErrorResponse(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String? = null
)
