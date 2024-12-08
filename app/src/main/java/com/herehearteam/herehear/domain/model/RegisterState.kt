package com.herehearteam.herehear.domain.model

data class RegisterState(
    val isRegistrationSuccessful: Boolean = false,
    val registrationError: String? = null,
    val currentUser: User? = null,
    val isLoading: Boolean = false
)