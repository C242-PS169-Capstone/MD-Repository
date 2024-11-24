package com.herehearteam.herehear.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient

class HomeViewModelFactory(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(googleAuthUiClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}