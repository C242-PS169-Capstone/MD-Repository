package com.herehearteam.herehear.ui.screens.profile

import RegisterViewModel
import android.app.Application
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.ui.screens.auth.LoginViewModel
import com.herehearteam.herehear.ui.screens.auth.LoginViewModelFactory
import com.herehearteam.herehear.ui.screens.auth.RegisterViewModelFactory
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