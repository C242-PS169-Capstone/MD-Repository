package com.herehearteam.herehear.ui.screens.predict

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.herehearteam.herehear.data.local.repository.PredictionRepository
import com.herehearteam.herehear.data.remote.response.PredictionResponse
import com.herehearteam.herehear.domain.repository.UserRepository
import com.herehearteam.herehear.ui.components.showJournalNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PredictionViewModel(
    private val predictionRepository: PredictionRepository
) : ViewModel() {
    private val _predictionResult = MutableStateFlow<PredictionResponse?>(null)
    val predictionResult = _predictionResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun predict(text: String, model: String = "both", context: Context) {
        viewModelScope.launch {
            _predictionResult.value = null
            _error.value = null

            val result = predictionRepository.predictText(text, model)
            result.onSuccess { prediction ->
                _predictionResult.value = prediction
                onTestClick(context)
            }.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    private fun onTestClick(context: Context) {
        viewModelScope.launch {
            showJournalNotification(context)
        }
    }
}