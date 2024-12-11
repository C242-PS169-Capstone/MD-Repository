package com.herehearteam.herehear.data.remote.response

data class EmergencyContactResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: EmergencyContactData?
)

data class EmergencyContactData(
    val emergency_id: Int,
    val emergency_name: String,
    val emergency_number: String,
    val relationship: String,
    val user_id: String
)