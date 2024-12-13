package com.herehearteam.herehear.ui.screens.archive

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herehearteam.herehear.data.local.repository.JournalRepository
import com.herehearteam.herehear.domain.model.Journal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class JournalArchiveState(
    val journals: List<Journal> = emptyList(),
    val selectedMonth: Int = LocalDateTime.now().monthValue - 1,
    val selectedDate: LocalDate? = null,
    val searchQuery: String = ""
)

class ArchiveViewModel(
    private val journalRepository: JournalRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(JournalArchiveState())
    val uiState: StateFlow<JournalArchiveState> = _uiState.asStateFlow()

    constructor(application: Application) : this(
        JournalRepository(application)
    )

    init {
        fetchData()
    }

    fun fetchData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        Log.d("ArchiveViewModelGRRAAH", "User ID: $userId")

        if (userId != null) {
            viewModelScope.launch {
                try {
                    Log.d("ArchiveViewModelBACOT", "Fetching journals for user: $userId")
                    journalRepository.fetchJournals(userId)
                        .collect { result ->
                            result.onSuccess { journalEntities ->
                                // Convert JournalEntity to Journal
                                val journals = journalEntities.map { entity ->
                                    Journal(
                                        id = entity.journalId,
                                        content = entity.content ?: "",
                                        dateTime = entity.createdDate ?: LocalDateTime.now(),
                                        question = entity.question ?: "No Question",
                                        userId = entity.userId
                                    )
                                }

                                _uiState.update { currentState ->
                                    currentState.copy(
                                        journals = journals
                                    )
                                }
                            }.onFailure { exception ->
                                // Handle error scenario
                                Log.e("fetchData", "Error: ${exception.message}")
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        journals = emptyList()
                                    )
                                }
                            }
                        }
                } catch (e: Exception) {
                    // Menangani exception yang terjadi di luar fetchJournals
                    Log.e("fetchData", "Unexpected error: ${e.message}")
                    _uiState.update { currentState ->
                        currentState.copy(
                            journals = emptyList()
                        )
                    }
                }
            }
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