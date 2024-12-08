package com.herehearteam.herehear.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.domain.model.LoginState
import com.herehearteam.herehear.domain.model.SignInResult
import com.herehearteam.herehear.domain.model.User
import com.herehearteam.herehear.domain.model.UserData
import com.herehearteam.herehear.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginViewModel(
    private val userRepository: UserRepository,
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val firebaseAuth = Firebase.auth

    fun loginWithEmailPassword(email: String, password: String) {
        _state.update { it.copy(isLoading = true, signInError = null) }
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    try {
                        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                        val currentUser = authResult.user

                        if (currentUser != null) {
                            val userData = User(
                                userId = currentUser.uid,
                                displayName = currentUser.displayName ?: "",
                                email = currentUser.email ?: ""
                            )

                            userRepository.saveUser(userData)

                            SignInResult(userData, null)
                        } else {
                            SignInResult(null, "Login gagal. Silakan coba lagi.")
                        }
                    } catch (e: Exception) {
                        SignInResult(null, e.localizedMessage ?: "Login gagal. Pastikan email dan password benar.")
                    }
                }
                _state.update {
                    it.copy(
                        isSignInSuccessful = result.data != null,
                        signInError = result.errorMessage,
                        currentUser = result.data,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSignInSuccessful = false,
                        signInError = "Terjadi kesalahan. Silakan coba lagi.",
                        isLoading = false
                    )
                }
            }
        }
    }

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

    fun resetLoginState() {
        _state.update {
            val newState = it.copy(
                isSignInSuccessful = false,
                signInError = null,
                currentUser = null
            )
            Log.d("LoginViewModel", "resetLoginState: $newState")
            newState
        }
    }
}
