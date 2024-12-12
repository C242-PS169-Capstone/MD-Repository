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
import com.herehearteam.herehear.data.remote.api.ApiService
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.ui.screens.auth.LoginViewModel
import com.herehearteam.herehear.ui.screens.auth.LoginViewModelFactory
import com.herehearteam.herehear.ui.screens.auth.RegisterViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val emergencyContactRepository: EmergencyContactRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    private val apiService = ApiConfig.getApiService()

//    private val _uiState = MutableStateFlow(ProfileUiState())
//    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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
                    val emergencyEntity = EmergencyEntity(
                        userId = id,
                        namaKontak = contact.emergency_name,
                        nomorTelepon = contact.emergency_number,
                        hubungan = contact.relationship
                    )

                    emergencyContactRepository.insertOrUpdateEmergencyContact(emergencyEntity)

                    val existingContacts = apiService.getAllEmergencyContacts()
                    val userEmergencyContacts = existingContacts.data.filter { contact ->
                        contact.user_id == id
                    }

                    val apiResponse = if (userEmergencyContacts.isEmpty()) {
                        val apiRequest = EmergencyContactRequest(
                            emergency_name = contact.emergency_name,
                            emergency_number = contact.emergency_number,
                            relationship = contact.relationship,
                            user_id = id
                        )
                        apiService.createEmergencyContact(apiRequest)
                    } else {
                        val existingEmergencyId = userEmergencyContacts.first().emergency_id

                        val updateRequest = EmergencyContactUpdateRequest(
                            emergency_name = contact.emergency_name,
                            emergency_number = contact.emergency_number,
                            relationship = contact.relationship,
                            user_id = id
                        )

                        apiService.updateEmergencyContact(
                            existingEmergencyId,
                            updateRequest
                        )
                    }

                    if (apiResponse.status && apiResponse.data != null) {
                        // Cari emergency contact di local database berdasarkan user_id
                        val existingLocalContact = emergencyContactRepository.getEmergencyContact(id)

                        // Simpan Ke Lokal Database
                        val emergencyEntity = existingLocalContact?.let {
                            EmergencyEntity(
                                id = it.id,
                                userId = id,
                                namaKontak = contact.emergency_name,
                                nomorTelepon = contact.emergency_number,
                                hubungan = contact.relationship
                            )
                        }

                        // Update atau insert ke local database
                        if (emergencyEntity != null) {
                            emergencyContactRepository.insertOrUpdateEmergencyContact(emergencyEntity)
                        }
                    } else {
                        throw Exception(apiResponse.message ?: "Gagal menyimpan kontak")
                    }

                    loadEmergencyContact()
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Error saving emergency contact", e)
                }
            } ?: run {
                Log.e("ProfileViewModel", "User ID is null, cannot save emergency contact")
            }
        }
    }

    fun loadEmergencyContact() {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            userId?.let { id ->
                try {
                    // Pertama, coba fetch dari API jika lokal kosong
                    emergencyContactRepository.fetchAndSaveEmergencyContact(id)

                    // Kemudian ambil dari lokal
                    val localContact = emergencyContactRepository.getEmergencyContact(id)

                    localContact?.let { contact ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                emergencyContact = EmergencyContact(
                                    userId = contact.userId,
                                    emergency_name = contact.namaKontak ?: "",
                                    emergency_number = contact.nomorTelepon ?: "",
                                    relationship = contact.hubungan ?: ""
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Error loading emergency contact", e)
                    // Optional: Set error state if needed
                }
            }
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
    val emergency_id: Int? = null,
    val userId: String = "",
    val emergency_name: String = "",
    val emergency_number: String = "",
    val relationship: String = ""
)