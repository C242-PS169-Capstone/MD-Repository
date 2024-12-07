package com.herehearteam.herehear.domain.model

data class User(
    val userId: String,
    val email: String,
    val displayName: String? = null,
    val idToken: String? = null,
)