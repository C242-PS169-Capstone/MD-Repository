package com.herehearteam.herehear.domain.model

data class LoginState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)