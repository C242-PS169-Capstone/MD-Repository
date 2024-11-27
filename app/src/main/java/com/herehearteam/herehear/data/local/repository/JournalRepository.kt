package com.herehearteam.herehear.data.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.herehearteam.herehear.data.local.dao.JournalDao
import com.herehearteam.herehear.data.local.database.JournalRoomDatabase
import com.herehearteam.herehear.data.local.entity.JournalEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class JournalRepository(application: Application) {
    private val mJournalDao: JournalDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = JournalRoomDatabase.getDatabase(application)
        mJournalDao = db.journalDao()
    }

    fun insertJournal(journal: JournalEntity) {
        mJournalDao.insertJournal(journal)
    }

    fun updateJournal(journal: JournalEntity) {
        executorService.execute {
            mJournalDao.updateJournal(journal)
        }
    }

    fun deleteJournal(journal: JournalEntity) {
        executorService.execute {
            mJournalDao.deleteJournal(journal)
        }
    }

    fun getAllJournals(): LiveData<List<JournalEntity>> = mJournalDao.getAllJournals()
}
