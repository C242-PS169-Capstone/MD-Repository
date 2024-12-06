package com.herehearteam.herehear.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.domain.repository.UserRepository

class LoginViewModelFactory(
    private val userRepository: UserRepository,
    private val googleAuthClient: GoogleAuthUiClient
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository, googleAuthClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}