package com.herehearteam.herehear.ui.screens.profile

import RegisterViewModel
import android.app.Application
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.local.entity.EmergencyEntity
import com.herehearteam.herehear.data.local.repository.EmergencyContactRepository
import com.herehearteam.herehear.data.model.EmergencyContactRequest
import com.herehearteam.herehear.data.model.EmergencyContactUpdateRequest
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.ui.screens.auth.LoginViewModel
import com.herehearteam.herehear.ui.screens.auth.LoginViewModelFactory
import com.herehearteam.herehear.ui.screens.auth.RegisterViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val emergencyContactRepository: EmergencyContactRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val apiService = ApiConfig.getApiService()

    init {
        loadUserData()
        loadEmergencyContact()
    }

    private fun loadUserData() {
        val userData = googleAuthUiClient.getSignedInUser()
        _uiState.update { currentState ->
            currentState.copy(
                userName = userData?.displayName ?: "User",
            )
        }
    }

    fun saveEmergencyContact(contact: EmergencyContact) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        viewModelScope.launch {
            userId?.let { id ->
                try {
                    // Simpan ke lokal terlebih dahulu
                    val emergencyEntity = EmergencyEntity(
                        userId = id,
                        namaKontak = contact.emergency_name,
                        nomorTelepon = contact.emergency_number,
                        hubungan = contact.relationship
                    )
                    emergencyContactRepository.insertEmergencyContact(emergencyEntity)

                    val apiRequest = EmergencyContactRequest(
                        emergency_id = 5,
                        emergency_name = contact.emergency_name,
                        emergency_number = contact.emergency_number,
                        relationship = contact.relationship,
                        user_id = id
                    )

                    val apiResponse = if (uiState.value.emergencyContact == null) {
                        // Jika belum ada contact, lakukan create
                        apiService.createEmergencyContact(apiRequest)
                    } else {
                        // Jika sudah ada contact, lakukan update
                        val updateRequest = EmergencyContactUpdateRequest(
                            emergency_name = contact.emergency_name,
                            emergency_number = contact.emergency_number,
                            relationship = contact.relationship,
                            user_id = id
                        )

                        apiService.updateEmergencyContact(
                            apiRequest.emergency_id.toString(),
                            updateRequest
                        )
                    }

                    if (apiResponse.status && apiResponse.data != null) {
                        // Update lokal dengan ID dari server jika create
                        if (uiState.value.emergencyContact == null) {
                            val updatedEntity = emergencyEntity.copy(
                                id = apiResponse.data.emergency_id
                            )
                            emergencyContactRepository.updateEmergencyContact(updatedEntity)
                        }
                    } else {
                        throw Exception(apiResponse.message ?: "Gagal menyimpan kontak")
                    }

                    loadEmergencyContact()
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Error saving emergency contact", e)
//                    _uiState.update {
//                        it.copy(
//                            error = "Gagal menyimpan kontak: ${e.localizedMessage}"
//                        )
//                    }
                }
            } ?: run {
                Log.e("ProfileViewModel", "User ID is null, cannot save emergency contact")
            }
        }
    }

    fun loadEmergencyContact() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        viewModelScope.launch {
            userId?.let { id ->
                val emergencyContact = emergencyContactRepository.getEmergencyContact(id)
                _uiState.update { currentState ->
                    currentState.copy(
                        emergencyContact = emergencyContact?.let {
                            EmergencyContact(
                                emergency_name = it.namaKontak ?: "",
                                emergency_number = it.nomorTelepon ?: "",
                                relationship = it.hubungan ?: ""
                            )
                        }
                    )
                }
            }
        }
    }

    private suspend fun updateEmergencyContact(entity: EmergencyEntity) {
        emergencyContactRepository.updateEmergencyContact(entity)
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
    val userId: String = "",
    val emergency_name: String = "",
    val emergency_number: String = "",
    val relationship: String = ""
)