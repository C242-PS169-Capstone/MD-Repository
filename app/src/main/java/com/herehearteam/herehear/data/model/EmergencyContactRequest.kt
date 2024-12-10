package com.herehearteam.herehear.data.model

data class EmergencyContactRequest(
    val emergency_id: Int? = null,
    val emergency_name: String,
    val emergency_number: String,
    val relationship: String,
    val user_id: String
)