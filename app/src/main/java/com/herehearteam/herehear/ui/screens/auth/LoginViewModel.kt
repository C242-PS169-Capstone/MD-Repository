package com.herehearteam.herehear.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.domain.model.LoginState
import com.herehearteam.herehear.domain.model.SignInResult
import com.herehearteam.herehear.domain.model.User
import com.herehearteam.herehear.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val googleAuthClient: GoogleAuthUiClient
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

    fun logout() {
        viewModelScope.launch {
            userRepository.clearUser()
            resetState()
        }
    }
}

//class LoginViewModel(
//    private val userRepository: UserRepository? = null,
//    private val googleAuthClient: GoogleAuthUiClient? = null
//) : ViewModel() {
//    private val _state = MutableStateFlow(LoginState())
//    val state = _state.asStateFlow()
//
//    constructor() : this(null, null)
//
//    fun onSignInResult(result: SignInResult) {
//        viewModelScope.launch {
//            if (result.data != null) {
//                userRepository?.saveUser(result.data)
//                _state.update {
//                    it.copy(
//                        isSignInSuccessful = true,
//                        signInError = null
//                    )
//                }
//            } else {
//                _state.update {
//                    it.copy(
//                        isSignInSuccessful = false,
//                        signInError = result.errorMessage
//                    )
//                }
//            }
//        }
//    }
//
//    fun resetState() {
//        _state.update { LoginState() }
//    }
//
//    companion object {
//        fun create(
//            userRepository: UserRepository,
//            googleAuthClient: GoogleAuthUiClient
//        ): LoginViewModel {
//            return LoginViewModel(userRepository, googleAuthClient)
//        }
//    }
//}
//
//data class LoginState(
//    val isSignInSuccessful: Boolean = false,
//    val signInError: String? = null
//)
