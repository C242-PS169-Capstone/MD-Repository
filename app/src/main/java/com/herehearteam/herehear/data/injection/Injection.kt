package com.herehearteam.herehear.data.injection

import android.app.Application
import android.content.Context
import com.herehearteam.herehear.data.local.repository.JournalRepository

object Injection {
    fun provideJournalRepository(context: Context): JournalRepository {
        val aplication = context.applicationContext as Application
        return JournalRepository.getInstance(aplication)
    }
}