package com.herehearteam.herehear.ui.screens.archive

import android.app.Application
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

    private fun fetchData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            viewModelScope.launch {
                journalRepository.getAllJournals(userId)
                    .collect { journalEntities ->
                        // Convert JournalEntity to Journal
                        val journals = journalEntities.map { entity ->
                            Journal(
                                id = entity.journalId,
                                content = entity.content,
                                dateTime = entity.createdDate,
                                question = entity.question,
                                userId = entity.userId
                            )
                        }

                        _uiState.update { currentState ->
                            currentState.copy(journals = journals)
                        }
                    }
            }
        } else {
            // Handle case when user is not logged in
            // Optionally clear journals or show login prompt
            _uiState.update { currentState ->
                currentState.copy(journals = emptyList())
            }
        }
    }

//    private fun fetchData() {
//        viewModelScope.launch {
//            journalRepository.getAllJournals().asLiveData().observeForever { journalEntities ->
//                // Convert JournalEntity to Journal
//                val journals = journalEntities.map { entity ->
//                    Journal(
//                        id = entity.journalId,
//                        content = entity.content,
//                        dateTime = entity.createdDate,
//                        question = entity.question
//                    )
//                }
//
//                _uiState.update { currentState ->
//                    currentState.copy(journals = journals)
//                }
//            }
//        }
//    }

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