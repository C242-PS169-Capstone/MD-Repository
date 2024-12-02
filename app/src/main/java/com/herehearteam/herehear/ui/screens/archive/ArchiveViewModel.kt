package com.herehearteam.herehear.ui.screens.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

data class JournalArchiveState(
    val journals: List<Journal> = emptyList(),
    val selectedMonth: Int = LocalDateTime.now().monthValue - 1,
    val selectedDate: LocalDate? = null,
    val searchQuery: String = ""
)

class ArchiveViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JournalArchiveState())
    val uiState: StateFlow<JournalArchiveState> = _uiState.asStateFlow()

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        _uiState.update { currentState ->
            currentState.copy(journals = DummyJournals.journalList)
        }
    }

    fun updateSelectedMonth(month: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedMonth = month)
        }
    }

    fun updateSelectedDate(date: LocalDate?) {
        _uiState.update { currentState ->
            currentState.copy(selectedDate = date)
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }
}