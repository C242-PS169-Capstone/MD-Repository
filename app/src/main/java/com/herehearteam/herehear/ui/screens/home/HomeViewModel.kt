package com.herehearteam.herehear.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herehearteam.herehear.domain.model.DayMood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeUiState(
    val userName: String = "",
    val dailyQuestion: String = "",
    val weeklyMoods: List<DayMood> = emptyList()
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(
                userName = "Arjadikrama",
                dailyQuestion = "Sebutkan isi pikiranmu akhir-akhir ini",
                weeklyMoods = createInitialWeeklyMoods()
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