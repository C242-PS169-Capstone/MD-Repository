package com.herehearteam.herehear.ui.screens.journal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.local.repository.JournalRepository
import com.herehearteam.herehear.data.local.repository.UserRepositoryImpl
import com.herehearteam.herehear.domain.repository.UserRepository

class JournalViewModelFactory private constructor(
    private val mApplication: Application,
    private val userRepository: UserRepository,
    private val journalRepository: JournalRepository
) : ViewModelProvider.Factory {
    companion object {
        @Volatile
        private var INSTANCE: JournalViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): JournalViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: JournalViewModelFactory(
                    application,
                    UserRepositoryImpl(
                        UserPreferencesDataStore.getInstance(application)
                    ),
                    JournalRepository.getInstance(application)
                ).also { INSTANCE = it }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JournalViewModel::class.java) ->
                JournalViewModel(mApplication, userRepository ,journalRepository) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}