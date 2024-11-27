package com.herehearteam.herehear.ui.screens.home

import androidx.lifecycle.ViewModel
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.domain.model.DayMood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class HomeUiState(
    val userName: String = "",
    val dailyQuestion: String = "",
    val weeklyMoods: List<DayMood> = emptyList(),
)

class HomeViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userData = googleAuthUiClient.getSignedInUser()
        _uiState.update { currentState ->
            currentState.copy(
                userName = userData?.username ?: "User",
            )
        }
    }

    private fun createInitialWeeklyMoods(): List<DayMood> {
        val today = LocalDate.now()
        return listOf("M", "S", "S", "R", "K", "J", "S").mapIndexed { index, day ->
            DayMood(
                day = day,
                mood = null,
                date = today.plusDays(index.toLong())
            )
        }
    }

    fun onFeatureClick(featureId: String) {

    }

    fun onDailyQuestionClick() {

    }

    fun onMoodClick(dayMood: DayMood) {

    }

    fun onNotificationClick() {

    }

    fun onSettingsClick() {

    }
}