package com.herehearteam.herehear.ui.screens.journal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.herehearteam.herehear.data.local.entity.JournalEntity
import com.herehearteam.herehear.data.local.helper.JournalHelper
import com.herehearteam.herehear.data.local.repository.JournalRepository
import com.herehearteam.herehear.domain.model.Journal
import com.herehearteam.herehear.domain.model.JournalQuestion
import com.herehearteam.herehear.domain.model.JournalQuestions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JournalViewModel(
    application: Application,
    private val journalRepository: JournalRepository
    ) : AndroidViewModel(application){
    private val mJournalRepository: JournalRepository = JournalRepository(application)

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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

    private val _shouldShowResetConfirmationDialog = MutableStateFlow(false)
    val shouldShowResetConfirmationDialog = _shouldShowResetConfirmationDialog.asStateFlow()

    private val _isSaveSuccessful = MutableStateFlow(false)
    val isSaveSuccessful = _isSaveSuccessful.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    init {
        refreshAllQuestions()
    }

    fun saveJournal() {
        viewModelScope.launch(Dispatchers.IO) {
            val question = _selectedQuestion.value?.text ?: ""
            val content = _memoText.value.takeIf { it.isNotBlank() }

            if (content != null) {
                val journalToSave = JournalEntity(
                    createdDate = JournalHelper.getCurrentDate(),
                    content = content,
                    question = question
                )
                mJournalRepository.insertJournal(journalToSave)

                withContext(Dispatchers.Main) {
                    _isSaveSuccessful.value = true
                    _navigationEvent.value = NavigationEvent.NavigateToHome
                    clearSelectedQuestion()
                    _isFabExpanded.value = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onBackPressed() {
        if (_memoText.value.isNotBlank()) {
            _shouldShowBackPressedDialog.value = true
        } else {
            _navigationEvent.value = NavigationEvent.NavigateBack
        }
    }

    fun onBackPressedConfirm() {
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

    fun showResetConfirmationDialog() {
        _shouldShowResetConfirmationDialog.value = true
    }

    fun cancelResetConfirmation() {
        _shouldShowResetConfirmationDialog.value = false
    }

    fun confirmReset() {
        _memoText.value = ""
        _shouldShowResetConfirmationDialog.value = false
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

    private fun refreshAllQuestions() {
        _currentQuestions.value = JournalQuestions.getRandomQuestions()
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

    fun getJournalById(id:Int?, onResult: (Journal?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = id?.let { journalRepository.getJournalById(it) }
            val journal = entity?.let {
                Journal(
                    id = it.journalId,
                    content = it.content,
                    dateTime = it.createdDate,
                    question = it.question
                )
            }
            withContext(Dispatchers.Main) {
                onResult(journal)
            }
        }
    }
}

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
    data object NavigateToHome : NavigationEvent()
}