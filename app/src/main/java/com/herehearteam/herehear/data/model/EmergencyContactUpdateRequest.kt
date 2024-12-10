package com.herehearteam.herehear.data.model

data class EmergencyContactUpdateRequest(
    val emergency_name: String,
    val emergency_number: String,
    val relationship: String,
    val user_id: String
)