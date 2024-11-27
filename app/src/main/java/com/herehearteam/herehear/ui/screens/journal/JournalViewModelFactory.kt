package com.herehearteam.herehear.ui.screens.journal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JournalViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.Factory {
    companion object {
        @Volatile
        private var INSTANCE: JournalViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): JournalViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: JournalViewModelFactory(application).also { INSTANCE = it }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JournalViewModel::class.java) ->
                JournalViewModel(mApplication) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}