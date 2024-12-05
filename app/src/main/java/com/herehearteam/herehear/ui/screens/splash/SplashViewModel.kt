package com.herehearteam.herehear.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herehearteam.herehear.domain.model.User
import com.herehearteam.herehear.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val userRepository: UserRepository? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    constructor() : this(null)

    init {
        if (userRepository != null) {
            checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            userRepository?.user?.collect { user ->
                _uiState.value = if (user != null) {
                    SplashUiState.Authenticated(user)
                } else {
                    SplashUiState.Unauthenticated
                }
            }
        }
    }

    companion object {
        fun create(userRepository: UserRepository): SplashViewModel {
            return SplashViewModel(userRepository)
        }
    }
}
sealed class SplashUiState {
    data object Loading : SplashUiState()
    data class Authenticated(val user: User) : SplashUiState()
    data object Unauthenticated : SplashUiState()
}