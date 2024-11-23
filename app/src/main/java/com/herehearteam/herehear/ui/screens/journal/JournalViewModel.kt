package com.herehearteam.herehear.ui.screens.journal

import androidx.lifecycle.ViewModel
import com.herehearteam.herehear.domain.model.JournalQuestion
import com.herehearteam.herehear.domain.model.JournalQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class JournalViewModel : ViewModel() {
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

    init {
        refreshAllQuestions()
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

    fun selectQuestion(question: JournalQuestion) {
        _selectedQuestion.value = question
    }

    fun updateMemoText(text: String) {
        _memoText.value = text
    }

    fun toggleFabExpanded() {
        _isFabExpanded.value = !_isFabExpanded.value
    }
}