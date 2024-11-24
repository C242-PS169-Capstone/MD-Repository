package com.herehearteam.herehear.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userData = googleAuthUiClient.getSignedInUser()
        _uiState.update { currentState ->
            currentState.copy(
                userName = userData?.username ?: "User",
                photoUrl = userData?.profilePictureUrl,
                userId = userData?.userId
            )
        }
    }

    suspend fun signOut() {
        googleAuthUiClient.signOut()
    }
}

data class ProfileUiState(
    val userName: String = "",
    val photoUrl: String? = null,
    val userId: String? = null
)