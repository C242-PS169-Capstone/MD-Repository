package com.herehearteam.herehear.ui.screens.journal

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.local.entity.JournalEntity
import com.herehearteam.herehear.data.local.helper.JournalHelper
import com.herehearteam.herehear.data.local.repository.JournalRepository
import com.herehearteam.herehear.data.local.repository.PredictionRepository
import com.herehearteam.herehear.data.model.JournalRequestDto
import com.herehearteam.herehear.data.model.JournalUpdateRequestDto
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    private val apiService = ApiConfig.getApiService()
    @SuppressLint("StaticFieldLeak")
    val context: Context = application.applicationContext

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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

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
            override fun onLost(network: Network) {
                _isNetworkAvailable.value = false
            }
        }
    }

    fun checkNetworkConnectivity() {
        val activeNetwork = connectivityManager.activeNetworkInfo
        _isNetworkAvailable.value = activeNetwork?.isConnectedOrConnecting == true
    }


    fun saveJournal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Set loading to true before starting save operation
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                    _saveError.value = null
                }

                val userId = _currentUser.value?.userId
                if (userId == null) {
                    withContext(Dispatchers.Main) {
                        _isSaveSuccessful.value = false
                        _isLoading.value = false
                        showErrorToast("Pengguna tidak terautentikasi")
                    }
                    return@launch
                }

                val question = _selectedQuestion.value?.text ?: ""
                val content = _memoText.value.takeIf { it.isNotBlank() }

                if (content != null) {
                    if (currentJournalId != null) {
                        try {
                            journalRepository.updateJournalById(
                                id = currentJournalId!!,
                                content = content,
                                userId = userId
                            )
                            val reqBody = JournalUpdateRequestDto(
                                content = content,
                                question = question
                            )
                            apiService.updateJournalById(currentJournalId!!, reqBody)
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                showErrorToast("Gagal memperbarui jurnal: ${e.localizedMessage}")
                                throw e
                            }
                        }
                    } else {
                        try {
                            val response = if (question.isNotBlank()) {
                                val remoteJournal = JournalRequestDto(
                                    content = content,
                                    question = question,
                                    user_id = userId
                                )
                                apiService.createJournal(remoteJournal)
                            } else {
                                val remoteJournalNoQuestion = JournalRequestDto(
                                    content = content,
                                    user_id = userId,
                                    question = "Perasaanmu hari ini"
                                )
                                apiService.createJournal(remoteJournalNoQuestion)
                            }

                            val predictionResult = predictionRepository.predictText(content)
                            val journalToSave = JournalEntity(
                                journalId = response.data.journalId,
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
                            journalRepository.insertJournal(journalToSave)
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                showErrorToast("Gagal membuat jurnal: ${e.localizedMessage}")
                                throw e
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
                        Toast.makeText(context, "berhasil", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showErrorToast("Konten jurnal tidak boleh kosong")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isSaveSuccessful.value = false
                    _saveError.value = e.localizedMessage ?: "Terjadi kesalahan tidak dikenal"
                    showErrorToast(_saveError.value ?: "Gagal menyimpan jurnal")
                    _navigationEvent.value = NavigationEvent.NavigateToHome
                }
            } finally {
                // Ensure loading is set to false
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
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
                    try {
                        // Panggil repository untuk delete
                        journalRepository.deleteJournalById(id, userId)

                        currentJournalId = null
                        originalJournalContent = ""

                        withContext(Dispatchers.Main) {
                            _navigationEvent.value = NavigationEvent.NavigateToHome

                            clearSelectedQuestion()
                            _isFabExpanded.value = false
                        }
                    } catch (e: Exception) {
                        Log.e("deleteJournal", "Failed to delete journal: ${e.message}")

                        withContext(Dispatchers.Main) {
                            // Opsional: Tampilkan pesan error ke user
                            showErrorToast("Failed to delete journal.")
                        }
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

            val entity = id?.let { journalRepository.getJournalById(it) }
            val journal = entity?.let {
                currentJournalId = it.data.journalId
                originalJournalContent = it.data.content
                Journal(
                    id = it.data.journalId,
                    content = it.data.content,
                    dateTime = LocalDate.parse(it.data.createdDate).atStartOfDay(),
                    question = it.data.question,
                    userId = userId
                )
            }

            withContext(Dispatchers.Main) {
                onResult(journal)
            }
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
    data object NavigateToHome : NavigationEvent()
}
