package com.herehearteam.herehear.ui.screens.journal

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.local.entity.JournalEntity
import com.herehearteam.herehear.data.local.helper.JournalHelper
import com.herehearteam.herehear.data.local.repository.JournalRepository
import com.herehearteam.herehear.data.local.repository.PredictionRepository
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.domain.model.Journal
import com.herehearteam.herehear.domain.model.JournalQuestion
import com.herehearteam.herehear.domain.model.JournalQuestions
import com.herehearteam.herehear.domain.model.User
import com.herehearteam.herehear.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JournalViewModel(
    application: Application,
    private val userRepository: UserRepository,
    private val journalRepository: JournalRepository
    ) : AndroidViewModel(application){
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var currentJournalId: Int? = null
    private var originalJournalContent: String? = ""
    private val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val modelApiService = ApiConfig.getApiModelService()
    private val predictionRepository = PredictionRepository(modelApiService)

//    private val currentUser = FirebaseAuth.getInstance().currentUser
//    private val userId = currentUser?.uid

    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val userId: String? get() = _currentUser.value?.userId

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible = _isBottomSheetVisible.asStateFlow()

    private val _selectedQuestion = MutableStateFlow<JournalQuestion?>(null)
    val selectedQuestion = _selectedQuestion.asStateFlow()

    private val _memoText = MutableStateFlow("")
    val memoText = _memoText.asStateFlow()

    private val _currentQuestions = MutableStateFlow<List<JournalQuestion>>(emptyList())
    val currentQuestions = _currentQuestions.asStateFlow()

    private val _isFabExpanded = MutableStateFlow(false)
    val isFabExpanded = _isFabExpanded.asStateFlow()

    private val _shouldShowBackPressedDialog = MutableStateFlow(false)
    val shouldShowBackPressedDialog = _shouldShowBackPressedDialog.asStateFlow()

    private val _shouldShowDeleteConfirmationDialog = MutableStateFlow(false)
    val shouldShowDeleteConfirmationDialog = _shouldShowDeleteConfirmationDialog.asStateFlow()

    private val _isSaveSuccessful = MutableStateFlow(false)
    val isSaveSuccessful = _isSaveSuccessful.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    init {
        checkNetworkConnectivity()
        refreshQuestions()
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _currentUser.value = user
                Log.d("JournalViewModel", "Current user updated: ${user?.userId}")
            }
        }

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isNetworkAvailable.value = true
                journalRepository.syncPendingJournals()
            }

            override fun onLost(network: Network) {
                _isNetworkAvailable.value = false
            }
        }
    }

    fun checkNetworkConnectivity() {
        val activeNetwork = connectivityManager.activeNetworkInfo
        _isNetworkAvailable.value = activeNetwork?.isConnectedOrConnecting == true

        // Sync pending journals if network is available on init
        if (_isNetworkAvailable.value) {
            journalRepository.syncPendingJournals()
        }
    }


    fun saveJournal() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = _currentUser.value?.userId
            if (userId == null) {
                withContext(Dispatchers.Main) {
                    _isSaveSuccessful.value = false
                }
                return@launch
            }

            val question = _selectedQuestion.value?.text ?: ""
            val content = _memoText.value.takeIf { it.isNotBlank() }

            if (content != null) {
                if (currentJournalId != null) {
                    journalRepository.updateJournalById(
                        id = currentJournalId!!,
                        content = content,
                        userId = userId
                    )
                } else {
                    val predictionResult = predictionRepository.predictText(content)
                    val journalToSave = JournalEntity(
                        createdDate = JournalHelper.getCurrentDate(),
                        content = content,
                        question = question,
                        userId = userId,
                        isPredicted = _isNetworkAvailable.value,
                        predict1Label = predictionResult.getOrNull()?.model1?.prediction.toString() ?: null,
                        predict1Confidence = predictionResult.getOrNull()?.model1?.confidence.toString() ?: null,
                        predict2Label = predictionResult.getOrNull()?.model2?.prediction.toString() ?: null,
                        predict2Confidence = predictionResult.getOrNull()?.model2?.confidence.toString() ?: null,
                    )
                    val savedJournalId = journalRepository.insertJournal(journalToSave)
                    if (!_isNetworkAvailable.value) {
                        // Optional: You might want to use a WorkManager job for this in a real app
                        viewModelScope.launch {
                            while (!_isNetworkAvailable.value) {
                                delay(30000) // Check every 30 seconds
                            }
                            journalRepository.syncPendingJournals()
                        }
                    }
                }

                currentJournalId = null
                originalJournalContent = ""

                withContext(Dispatchers.Main) {
                    _isSaveSuccessful.value = true
                    _navigationEvent.value = NavigationEvent.NavigateToHome
                    clearSelectedQuestion()
                    _isFabExpanded.value = false
                }
            } else {
//              handle
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onBackPressed() {
        val currentMemoText = _memoText.value.trim()
        val hasContentChanged = currentMemoText != originalJournalContent?.trim()

        if (hasContentChanged) {
            _shouldShowBackPressedDialog.value = true
        } else {
            _navigationEvent.value = NavigationEvent.NavigateBack
            clearSelectedQuestion()
            originalJournalContent = ""
            currentJournalId = null
        }
    }

    fun onBackPressedConfirm() {
        originalJournalContent = ""
        currentJournalId = null
        _navigationEvent.value = NavigationEvent.NavigateBack
        clearSelectedQuestion()
        _shouldShowBackPressedDialog.value = false
    }

    fun onBackPressedCancel() {
        _shouldShowBackPressedDialog.value = false
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun showDeleteConfirmationDialog() {
        _shouldShowDeleteConfirmationDialog.value = true
    }

    fun cancelDeleteConfirmation() {
        _shouldShowDeleteConfirmationDialog.value = false
        _isFabExpanded.value = false
    }

    fun confirmDelete() {
        deleteJournal()
        _shouldShowDeleteConfirmationDialog.value = false
        _isFabExpanded.value = false
    }

    fun resetSaveSuccessful() {
        _isSaveSuccessful.value = false
    }

    fun refreshSingleQuestion(index: Int) {
        val currentList = _currentQuestions.value.toMutableList()
        val remainingQuestions = JournalQuestions.questions.filter { it !in currentList }
        if (remainingQuestions.isNotEmpty()) {
            currentList[index] = remainingQuestions.random()
            _currentQuestions.value = currentList
        }
    }

    fun clearSelectedQuestion() {
        _selectedQuestion.value = null
        _memoText.value = ""
    }

    fun refreshQuestions() {
        _currentQuestions.value = JournalQuestions.getRandomQuestions()
    }

    fun showBottomSheet() {
        _isBottomSheetVisible.value = true
    }

    fun hideBottomSheet() {
        _isBottomSheetVisible.value = false
    }

    fun selectQuestion(questionObject: JournalQuestion?, questionText: String?) {
        val question: JournalQuestion?
        if(questionText != null){
            question = JournalQuestions.getQuestion(questionText)
        } else {
            question = questionObject
        }
        _selectedQuestion.value = question
    }

    fun updateMemoText(text: String) {
        _memoText.value = text
    }

    fun toggleFabExpanded() {
        _isFabExpanded.value = !_isFabExpanded.value
    }

    fun deleteJournal() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        currentJournalId?.let { id ->
            if (userId != null) {
                viewModelScope.launch(Dispatchers.IO) {
                    journalRepository.deleteJournalById(id, userId)
                    currentJournalId = null
                    originalJournalContent = ""

                    withContext(Dispatchers.Main) {
                        _navigationEvent.value = NavigationEvent.NavigateBack
                        clearSelectedQuestion()
                        _isFabExpanded.value = false
                    }
                }
            } else {
                Log.d("apadah", "error intinya")
            }
        }
    }

    fun getJournalById(id: Int?, onResult: (Journal?) -> Unit) {
        val userId = _currentUser.value?.userId
        viewModelScope.launch(Dispatchers.IO) {
            if (userId == null) {
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
                return@launch
            }

            val entity = id?.let { journalRepository.getJournalById(it, userId) }
            val journal = entity?.let {
                currentJournalId = it.journalId
                originalJournalContent = it.content
                Journal(
                    id = it.journalId,
                    content = it.content,
                    dateTime = it.createdDate,
                    question = it.question,
                    userId = userId
                )
            }

            withContext(Dispatchers.Main) {
                onResult(journal)
            }
        }
    }


//    fun getJournalById(id:Int?, onResult: (Journal?) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val entity = id?.let { journalRepository.getJournalById(it) }
//            val journal = entity?.let {
//                currentJournalId = it.journalId
//                originalJournalContent = it.content
//                Journal(
//                    id = it.journalId,
//                    content = it.content,
//                    dateTime = it.createdDate,
//                    question = it.question,
//                    userId =
//                )
//            }
//            withContext(Dispatchers.Main) {
//                onResult(journal)
//            }
//        }
//    }

//    fun saveJournal() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val question = _selectedQuestion.value?.text ?: ""
//            val content = _memoText.value.takeIf { it.isNotBlank() }
//
//            if (content != null) {
//                val userPreferences = UserPreferencesDataStore.userPreferencesFlow.firstOrNull()
//                val userId = userPreferences?.userId
//
//                if (userId != null) {
//                    if (currentJournalId != null) {
//                        mJournalRepository.updateJournalById(currentJournalId!!, content)
//                        currentJournalId = null
//                        originalJournalContent = ""
//                    } else {
//                        val journalToSave = JournalEntity(
//                            createdDate = JournalHelper.getCurrentDate(),
//                            content = content,
//                            question = question,
//                            userId = userId // Use userId from DataStore
//                        )
//                        mJournalRepository.insertJournal(journalToSave)
//                        currentJournalId = null
//                        originalJournalContent = ""
//                    }
//
//                    withContext(Dispatchers.Main) {
//                        _isSaveSuccessful.value = true
//                        _navigationEvent.value = NavigationEvent.NavigateToHome
//                        clearSelectedQuestion()
//                        _isFabExpanded.value = false
//                    }
//                } else {
//                    // Handle case where user is not logged in
//                    // For example, show an error message or redirect to login screen
//                    withContext(Dispatchers.Main) {
//                        _isSaveSuccessful.value = false
//                        // Handle user not logged in error
//                    }
//                }
//            }
//        }
//    }
}

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
    data object NavigateToHome : NavigationEvent()
}
