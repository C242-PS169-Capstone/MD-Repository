package com.herehearteam.herehear.domain.model

data class RegisterState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)