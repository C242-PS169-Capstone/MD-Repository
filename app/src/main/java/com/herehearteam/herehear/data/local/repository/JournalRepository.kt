package com.herehearteam.herehear.data.local.repository

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.herehearteam.herehear.data.local.dao.JournalDao
import com.herehearteam.herehear.data.local.database.AppDatabase
import com.herehearteam.herehear.data.local.entity.JournalEntity
import com.herehearteam.herehear.data.local.helper.Converters
import com.herehearteam.herehear.data.model.JournalRequestDto
import com.herehearteam.herehear.data.remote.api.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JournalRepository(application: Application) {
    private val mJournalDao: JournalDao
    private val journalApiService = ApiConfig.getApiService()
    private val modelApiService = ApiConfig.getApiModelService()
    private val connectivityManager = application.getSystemService(ConnectivityManager::class.java)
    private val predictionRepository = PredictionRepository(modelApiService)

    init {
        val db = AppDatabase.getDatabase(application)
        mJournalDao = db.journalDao()
    }

    private fun isNetworkAvailable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager?.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager?.activeNetworkInfo
            @Suppress("DEPRECATION")
            return networkInfo?.isConnected == true
        }
    }

    fun syncPendingJournals() {
        CoroutineScope(Dispatchers.IO).launch {
            if (isNetworkAvailable()) {
                val pendingJournals = mJournalDao.getPendingPredictionJournals()
                pendingJournals.forEach { journal ->
                    try {
                        val predictionResult = predictionRepository.predictText(journal.content)
                        Log.d("JournalRepositorySYNC", "Prediction result: $predictionResult")

                        mJournalDao.updateJournalPredictionsByJournalAndUserId(
                            journal.journalId,
                            journal.userId,
                            predictionResult.getOrNull()?.model1?.prediction.toString(),
                            predictionResult.getOrNull()?.model1?.confidence.toString(),
                            predictionResult.getOrNull()?.model2?.prediction.toString(),
                            predictionResult.getOrNull()?.model2?.confidence.toString(),
                            true
                        )
                    } catch (e: Exception) {
                        Log.e("JournalRepository", "Prediction sync failed", e)
                    }
                }

                val unsyncedJournals = mJournalDao.getUnsyncedJournals()
                unsyncedJournals.forEach { journal ->
                    try {
                        syncJournalToServer(journal)
                    } catch (e: Exception) {
                        // Log error or handle sync failure
                    }
                }
            }
        }
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
                    val predictionResult = predictionRepository.predictText(apiJournal.content!!)
                    JournalEntity(
                        journalId = apiJournal.journalId!!.toInt(),
                        content = apiJournal.content ?: "",
                        userId = userId,
                        createdDate = apiJournal.createdAt?.let {
                            LocalDateTime.parse(it, formatter)
                        } ?: LocalDateTime.now(),
                        question = null,
                        predict1Label = predictionResult.getOrNull()?.model1?.prediction.toString(),
                        predict1Confidence = predictionResult.getOrNull()?.model1?.confidence.toString(),
                        predict2Label = predictionResult.getOrNull()?.model2?.prediction.toString(),
                        predict2Confidence = predictionResult.getOrNull()?.model2?.confidence.toString(),
                    )
                }

                // Update local database with API journals
                withContext(Dispatchers.IO) {
                    // You might want to implement a more sophisticated sync strategy
                    // This example replaces local data with API data
                    mJournalDao.deleteAllJournalsForUser(userId)
                    mJournalDao.insertJournals(remoteJournals)
                }

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
                withContext(Dispatchers.IO) {
                    // Mark the journal as synced
                    mJournalDao.markJournalAsSynced(journal.journalId)
                }
            } else {
                // Server returned an error
            }
        } catch (e: Exception) {
            // Network or other error
           throw e
        }
    }

    suspend fun insertJournal(journal: JournalEntity) {
        val newJournal = if (isNetworkAvailable()) {
            // If online, mark as synced
            journal.copy(isSync = true)
        } else {
            // If offline, keep as unsynced
            journal.copy(isSync = false)
        }

        val localId = mJournalDao.insertJournal(newJournal)

        // Launch a coroutine to sync with server
        if (isNetworkAvailable()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val predictionResult = predictionRepository.predictText(newJournal.content)
                    Log.d("JournalRepositoryINSERT", "Prediction result: $predictionResult")
                    Log.d("JournalRepositoryINSERT", "Raw Prediction Result: $predictionResult")
                    Log.d("JournalRepositoryINSERT", "Prediction Result Nullable: ${predictionResult.getOrNull()}")
                    Log.d("JournalRepositoryINSERT", "Model1 Prediction: ${predictionResult.getOrNull()?.model1?.prediction}")
                    Log.d("JournalRepositoryINSERT", "Model1 Confidence: ${predictionResult.getOrNull()?.model1?.confidence}")
                    Log.d("JournalRepositoryINSERT", "Model2 Prediction: ${predictionResult.getOrNull()?.model2?.prediction}")
                    Log.d("JournalRepositoryINSERT", "Model2 Confidence: ${predictionResult.getOrNull()?.model2?.confidence}")

                    mJournalDao.updateJournalPredictionsByJournalAndUserId(
                        newJournal.journalId,
                        newJournal.userId,
                        predictionResult.getOrNull()?.model1?.prediction.toString(),
                        predictionResult.getOrNull()?.model1?.confidence.toString(),
                        predictionResult.getOrNull()?.model2?.prediction.toString(),
                        predictionResult.getOrNull()?.model2?.confidence.toString(),
                        true
                    )
                    syncJournalToServer(newJournal)
                } catch (e: Exception) {
                    // If sync fails, it remains unsynced and will be retried later
                }
            }
        }
        return localId
    }

    fun deleteJournalById(id: Int, userId: String) {
        mJournalDao.deleteJournalById(id, userId)
    }

    fun getLastPredictedJournal(userId: String): JournalEntity? {
        return mJournalDao.getLastPredictedJournal(userId)
    }

    fun getJournalById(id: Int, userId: String): JournalEntity? {
        return mJournalDao.getJournalByJournalId(id, userId)
    }

    fun updateJournalById(id: Int, content: String, userId: String) {
        mJournalDao.updateJournalContentById(id, content, userId)
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
