package com.herehearteam.herehear.data.local.repository

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.herehearteam.herehear.data.local.dao.JournalDao
import com.herehearteam.herehear.data.local.database.AppDatabase
import com.herehearteam.herehear.data.local.entity.JournalEntity
import com.herehearteam.herehear.data.model.JournalRequestDto
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.data.remote.response.ResponseJournal
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

//    fun syncPendingJournals() {
//        CoroutineScope(Dispatchers.IO).launch {
//            if (isNetworkAvailable()) {
//                val pendingJournals = mJournalDao.getPendingPredictionJournals()
//                pendingJournals.forEach { journal ->
//                    try {
//                        val predictionResult = predictionRepository.predictText(journal.content)
//                        Log.d("JournalRepositorySYNC", "Prediction result: $predictionResult")
//
//                        mJournalDao.updateJournalPredictionsByJournalAndUserId(
//                            journal.journalId,
//                            journal.userId,
//                            predictionResult.getOrNull()?.model1?.prediction.toString(),
//                            predictionResult.getOrNull()?.model1?.confidence.toString(),
//                            predictionResult.getOrNull()?.model2?.prediction.toString(),
//                            predictionResult.getOrNull()?.model2?.confidence.toString(),
//                            true
//                        )
//                    } catch (e: Exception) {
//                        Log.e("JournalRepository", "Prediction sync failed", e)
//                    }
//                }
//
//                val unsyncedJournals = mJournalDao.getUnsyncedJournals()
//                unsyncedJournals.forEach { journal ->
//                    try {
//                        syncJournalToServer(journal)
//                    } catch (e: Exception) {
//                        // Log error or handle sync failure
//                    }
//                }
//            }
//        }
//    }

    fun fetchJournals(userId: String): Flow<Result<List<JournalEntity>>> = flow {
        Log.d("JournalRepositoAUAHAAHAHA", "Fetching journals for user: $userId")
        try {
            // Fetch from remote API
            val apiResponse = journalApiService.getAllJournals()
            Log.d("JournalRepositoryANGGUR", "API Response: $apiResponse")

            if (apiResponse.status) {
                val userJournals = apiResponse.data.filter { journal ->
                    journal.userId == userId
                }
                Log.d("JournalRepositoryANJINGGG", "API Response: $userJournals")

                // Convert API journals to local entities
                val formatter = DateTimeFormatter.ISO_DATE_TIME
                val remoteJournals = userJournals.map { apiJournal ->
//                    val predictionResult = predictionRepository.predictText(apiJournal.content!!)
                    JournalEntity(
                        journalId = apiJournal.journalId,
                        content = apiJournal.content,
                        userId = userId,
                        createdDate = apiJournal.createdDate?.let {
                            LocalDateTime.parse(it, formatter)
                        } ?: LocalDateTime.now(),
                        question = apiJournal.question,
                        predict1Label = "Tidak ada prediksi",
                        predict1Confidence = "Tidak ada prediksi",
                        predict2Label = "Tidak ada prediksi",
                        predict2Confidence = "Tidak ada prediksi"
                    )
                }

                // Update local database with API journals
                withContext(Dispatchers.IO) {
                    Log.d("JournalRepositoryHEHEHE", "Remote Journals: $remoteJournals")
                  //  mJournalDao.deleteAllJournalsForUser(userId)
                    Log.d("JournalRepositoryHOHOHOHO", "Remote Journals: $remoteJournals")
//                    mJournalDao.insertJournals(remoteJournals)
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
                content = journal.content,
                user_id = journal.userId,
                question = journal.question
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

    fun insertJournal(journal: JournalEntity) {
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
                    mJournalDao.updateJournalPredictionsByJournalAndUserId(
                        newJournal.journalId,
                        newJournal.userId,
                        predictionResult.getOrNull()?.model1?.prediction.toString(),
                        predictionResult.getOrNull()?.model1?.confidence.toString(),
                        predictionResult.getOrNull()?.model2?.prediction.toString(),
                        predictionResult.getOrNull()?.model2?.confidence.toString(),
                        true
                    )
//                    syncJournalToServer(newJournal)
                } catch (e: Exception) {
                    // If sync fails, it remains unsynced and will be retried later
                }
            }
        }
        return localId
    }

    suspend fun deleteJournalById(id: Int, userId: String) {
        mJournalDao.deleteJournalById(id, userId)
        journalApiService.deleteJournalById(id)
    }

    fun getLastPredictedJournal(userId: String): JournalEntity? {
        return mJournalDao.getLastPredictedJournal(userId)
    }

    suspend fun getJournalById(id: Int): ResponseJournal {
        return journalApiService.getJournalById(id)
    }


    fun updateJournalById(id: Int, content: String, userId: String) {
        mJournalDao.updateJournalContentById(id, content, userId)
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
