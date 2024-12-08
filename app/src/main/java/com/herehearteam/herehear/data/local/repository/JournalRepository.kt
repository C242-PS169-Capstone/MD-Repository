package com.herehearteam.herehear.data.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.herehearteam.herehear.data.local.dao.JournalDao
import com.herehearteam.herehear.data.local.database.JournalRoomDatabase
import com.herehearteam.herehear.data.local.entity.JournalEntity
import com.herehearteam.herehear.data.model.JournalRequestDto
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.data.remote.api.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class JournalRepository(application: Application) {
    private val mJournalDao: JournalDao
    private val journalApiService = ApiConfig.getApiService()

    init {
        val db = JournalRoomDatabase.getDatabase(application)
        mJournalDao = db.journalDao()
    }

    fun fetchJournals(userId: String): Flow<Result<List<JournalEntity>>> = flow {
        try {
            // First, emit local journals
            val localJournals = mJournalDao.getAllJournalsFlow(userId).collect { localData ->
                emit(Result.success(localData))
            }

            // Fetch from remote API
            val apiResponse = journalApiService.getAllJournals(userId)

            if (apiResponse.status) {
                // Convert API journals to local entities
                val formatter = DateTimeFormatter.ISO_DATE_TIME
                val remoteJournals = apiResponse.data.map { apiJournal ->
                    JournalEntity(
                        journalId = apiJournal.journalId!!.toInt(),
                        content = apiJournal.content ?: "",
                        userId = userId,
                        createdDate = apiJournal.createdAt?.let {
                            LocalDateTime.parse(it, formatter)
                        } ?: LocalDateTime.now(),
                        question = null
                    )
                }

                // Update local database with API journals
//                withContext(Dispatchers.IO) {
//                    // You might want to implement a more sophisticated sync strategy
//                    // This example replaces local data with API data
//                    mJournalDao.deleteAllJournalsForUser(userId)
//                    mJournalDao.insertJournals(remoteJournals)
//                }

                // Emit updated data
                emit(Result.success(remoteJournals))
            } else {
                // API returned an error, but we still want to show local data
                emit(Result.failure(Exception(apiResponse.message)))
            }
        } catch (e: Exception) {
            // Network or other error, emit local data with an error
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun syncJournalToServer(journal: JournalEntity) {
        try {
            // Ensure you have a journal class ID if required by your API
            val request = JournalRequestDto(
                journal_id = journal.journalId.toString(),
                content = journal.content,
                journal_class_id = "1"
                //journal_class_id = journal.journalClassId?.toString() ?: ""
            )

            val response = journalApiService.createJournal(request)

            if (response.status) {
                // Successfully synced to server
                val updatedJournal = journal.copy(
                    // Update fields from server response if needed
                    // For example, you might want to update the createdAt timestamp
                    // createdAt = response.data.createdAt
                )

                withContext(Dispatchers.IO) {
                    mJournalDao.updateJournal(updatedJournal)
                }
            } else {
                // Server returned an error
                markJournalAsPendingSync(journal)
            }
        } catch (e: Exception) {
            // Network or other error
            markJournalAsPendingSync(journal)
            // Optionally log the error
            // Log.e("JournalRepository", "Sync failed", e)
        }
    }

    fun insertJournal(journal: JournalEntity) {
        val localId = mJournalDao.insertJournal(journal)

        // Launch a coroutine to sync with server
        CoroutineScope(Dispatchers.IO).launch {
            syncJournalToServer(journal)
        }
    }

    private fun markJournalAsPendingSync(journal: JournalEntity) {
        // You might want to add a 'pending_sync' flag to your JournalEntity
        val pendingSyncJournal = journal.copy(
            // Add a field to mark as pending sync
            // For example: isPendingSync = true
        )
        mJournalDao.updateJournal(pendingSyncJournal)
    }

    fun updateJournal(journal: JournalEntity) {
        mJournalDao.updateJournal(journal)
    }

    fun deleteJournal(journal: JournalEntity) {
        mJournalDao.deleteJournal(journal)
    }

    fun deleteJournalById(id: Int, userId: String) {
        mJournalDao.deleteJournalById(id, userId)
    }

    fun getAllJournals(userId: String): Flow<List<JournalEntity>> {
        return mJournalDao.getAllJournalsFlow(userId).flowOn(Dispatchers.IO)
    }

    fun getJournalById(id: Int, userId: String): JournalEntity? {
        return mJournalDao.getJournalByJournalId(id, userId)
    }

    fun updateJournalById(id: Int, content: String, userId: String) {
        mJournalDao.updateJournalContentById(id, content, userId)
    }

//    fun deleteJournalById(id: Int) {
//        mJournalDao.deleteJournalById(id)
//    }

//    fun getAllJournals(): Flow<List<JournalEntity>> {
//      return mJournalDao.getAllJournalsFlow().flowOn(Dispatchers.IO)
//    }

//    fun getJournalById(id: Int): JournalEntity? {
//       return mJournalDao.getJournalByJournalId(id)
//    }
//
//    fun updateJournalById(id: Int, content: String) {
//        mJournalDao.updateJournalContentById(id, content)
//    }

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
