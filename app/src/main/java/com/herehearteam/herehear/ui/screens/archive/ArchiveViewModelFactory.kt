package com.herehearteam.herehear.ui.screens.archive

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herehearteam.herehear.data.injection.Injection
import com.herehearteam.herehear.data.local.repository.JournalRepository

class ArchiveViewModelFactory(
    private val journalRepository: JournalRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchiveViewModel::class.java)) {
            // Create and return the ArchiveViewModel with the repository
            return ArchiveViewModel(journalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: ArchiveViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ArchiveViewModelFactory {
            if (instance == null) {
                synchronized(ArchiveViewModelFactory::class.java) {
                    instance = ArchiveViewModelFactory(Injection.provideJournalRepository(context))
                }
            }
            return instance as ArchiveViewModelFactory
        }
    }
}

