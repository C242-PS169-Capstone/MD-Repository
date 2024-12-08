package com.herehearteam.herehear.data.model

data class PredictionRequest(
    val text: String,
    val model: String // "model1", "model2", or "both"
)
