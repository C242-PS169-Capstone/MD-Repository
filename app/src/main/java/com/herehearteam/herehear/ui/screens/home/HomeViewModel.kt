package com.herehearteam.herehear.ui.screens.home

import androidx.lifecycle.ViewModel
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.domain.model.DayMood
import com.herehearteam.herehear.domain.model.User
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

    fun loadUserData() {
        val userData = googleAuthUiClient.getSignedInUser()
        _uiState.update { currentState ->
            currentState.copy(
                userName = userData?.displayName ?: "There",  // Set the user name
//                email = userData?.email ?: "",               // Set the email
//                userId = userData?.userId ?: ""              // Set the user ID
            )
        }
    }


//    private fun loadUserData() {
//        val userData = googleAuthUiClient.getSignedInUser()
//        _uiState.update { currentState ->
//            currentState.copy(
//                userName = User?.username ?: "User",
//            )
//        }
//    }


    fun onDailyQuestionClick() {

    }

    fun onNotificationClick() {

    }

    fun onSettingsClick() {

    }
}