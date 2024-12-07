package com.herehearteam.herehear.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herehearteam.herehear.domain.model.LoginState
import com.herehearteam.herehear.domain.model.SignInResult
import com.herehearteam.herehear.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        viewModelScope.launch {
            if (result.data != null) {
                userRepository.saveUser(result.data)
                _state.update {
                    it.copy(
                        isSignInSuccessful = true,
                        signInError = null,
                        currentUser = result.data
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isSignInSuccessful = false,
                        signInError = result.errorMessage,
                        currentUser = null
                    )
                }
            }
        }
    }

    fun resetState() {
        _state.update { LoginState() }
    }
}