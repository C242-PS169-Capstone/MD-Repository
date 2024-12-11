package com.herehearteam.herehear.data.model

data class DataUser (
    val user_id: String,
    val username: String,
    val email: String,
    val password: String? = null
)

data class UserRequestDto(
    val user_id: String,
    val username: String,
    val email: String,
    val password: String?
)