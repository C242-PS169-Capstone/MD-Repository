package com.herehearteam.herehear.ui.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herehearteam.herehear.data.local.database.AppDatabase
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.local.repository.EmergencyContactRepository
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient

//class ProfileViewModelFactory(
//    private val googleAuthUiClient: GoogleAuthUiClient,
//    private val userPreferencesDataStore: UserPreferencesDataStore
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ProfileViewModel(googleAuthUiClient, userPreferencesDataStore) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

class ProfileViewModelFactory(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            val emergencyDao = database.emergencyDao()
            val emergencyContactRepository = EmergencyContactRepository(emergencyDao)

            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(
                googleAuthUiClient,
                userPreferencesDataStore,
                emergencyContactRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
