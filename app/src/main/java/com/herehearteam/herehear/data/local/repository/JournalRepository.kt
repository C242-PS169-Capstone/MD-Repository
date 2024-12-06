package com.herehearteam.herehear.data.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.herehearteam.herehear.data.local.dao.JournalDao
import com.herehearteam.herehear.data.local.database.JournalRoomDatabase
import com.herehearteam.herehear.data.local.entity.JournalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class JournalRepository(application: Application) {
    private val mJournalDao: JournalDao

    init {
        val db = JournalRoomDatabase.getDatabase(application)
        mJournalDao = db.journalDao()
    }

    fun insertJournal(journal: JournalEntity) {
        mJournalDao.insertJournal(journal)
    }

    fun updateJournal(journal: JournalEntity) {
            mJournalDao.updateJournal(journal)
    }

    fun deleteJournal(journal: JournalEntity) {
            mJournalDao.deleteJournal(journal)
    }

    fun deleteJournalById(id: Int) {
        mJournalDao.deleteJournalById(id)
    }

    fun getAllJournals(): Flow<List<JournalEntity>> {
      return mJournalDao.getAllJournalsFlow().flowOn(Dispatchers.IO)
    }

    fun getJournalById(id: Int): JournalEntity? {
       return mJournalDao.getJournalByJournalId(id)
    }

    fun updateJournalById(id: Int, content: String) {
        mJournalDao.updateJournalContentById(id, content)
    }

    companion object{
        @Volatile
        private var instance: JournalRepository? = null

        fun getInstance(application: Application): JournalRepository {
            return instance ?: synchronized(this) {
                instance ?: JournalRepository(application).also { instance = it }
            }
        }
    }

}
