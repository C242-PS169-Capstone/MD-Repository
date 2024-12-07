package com.herehearteam.herehear.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _isEmergencyBottomSheetVisible = MutableStateFlow(false)
    val isEmergencyBottomSheetVisible = _isEmergencyBottomSheetVisible.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userData = googleAuthUiClient.getSignedInUser()
        _uiState.update { currentState ->
            currentState.copy(
                userName = userData?.displayName ?: "User",  // Set the user name
//                email = userData?.email ?: "",               // Set the email
//                userId = userData?.userId ?: ""              // Set the user ID
            )
        }
    }

    fun saveEmergencyContact(contact: EmergencyContact) {
        _uiState.update { currentState ->
            currentState.copy(
                emergencyContact = contact
            )
        }
    }

    fun showEmergencyBottomSheet() {
        if (!_isEmergencyBottomSheetVisible.value) {
            _isEmergencyBottomSheetVisible.value = true
        }
    }

    fun hideEmergencyBottomSheet() {
        if (_isEmergencyBottomSheetVisible.value) {
            _isEmergencyBottomSheetVisible.value = false
        }
    }

    suspend fun signOut() {
        googleAuthUiClient.signOut()
        userPreferencesDataStore.clearUser()
        _uiState.update {
            ProfileUiState()
        }
        Log.d("ProfileViewModel", "User logged out and state reset")
    }
}

data class ProfileUiState(
    val userName: String = "",
    val photoUrl: String? = null,
    val userId: String? = null,
    val emergencyContact: EmergencyContact? = null
)

data class EmergencyContact(
    var emergency_name: String,
    var emergency_number: String,
    var relationship: String
)