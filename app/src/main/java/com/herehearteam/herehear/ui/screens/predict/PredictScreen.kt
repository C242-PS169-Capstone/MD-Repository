package com.herehearteam.herehear.ui.screens.predict

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.auth.FirebaseAuth
import com.herehearteam.herehear.data.local.entity.JournalEntity
import com.herehearteam.herehear.data.local.repository.JournalRepository
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.herehearteam.herehear.R
import com.herehearteam.herehear.ui.components.CustomButtonFilled
import com.herehearteam.herehear.ui.components.PredictionLabelComponent

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PredictScreen(
    onBackClick: () -> Unit,
    journalRepository: JournalRepository,
    onArtikelClick: () -> Unit,
//    onHotlineClick: () -> Unit,
//    onEmergencyClick: () -> Unit
//    predictionViewModel: PredictionViewModel
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    val context = LocalContext.current
    val lastPredictedJournalState = remember { mutableStateOf<JournalEntity?>(null) }
    var lastPredictedJournal = lastPredictedJournalState.value
    // Coroutine scope for launching background operations
    val coroutineScope = rememberCoroutineScope()
    //val predictionResult by predictionViewModel.predictionResult.collectAsState()

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            // Query the database for the last journal with isPredicted = true
            val predictedJournals = withContext(Dispatchers.IO) {
                // Assuming you want to add a method to JournalDao to get the last predicted journal
                // If not, you can modify the query in the DAO
                journalRepository.getLastPredictedJournal(userId!!)
            }

            // Update the state with the last predicted journal
            lastPredictedJournalState.value = predictedJournals
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            CustomTopAppBar(
                pageTitle = "Prediction",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onIconClick = onBackClick
            )
        }
        Box(
            contentAlignment = Alignment.Center
        ) {
            lastPredictedJournal?.let { journal ->
                val model1Prediction = journal.predict1Label
                val model1Confidence = journal.predict1Confidence
                val model2Prediction = journal.predict2Label
                val model2Confidence = journal.predict2Confidence
                val (emoticon, gifRes) = when {
                    model1Prediction == "Suicidal" ->
                        "Sad" to R.drawable.greeting_gif

                    else ->
                        "Happy" to R.drawable.greeting_gif
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    GlideImage(
                        model = gifRes,
                        contentDescription = "$emoticon Emoticon",
                        modifier = Modifier
                            .size(250.dp)
                    )
                    // Greeting Text
                    Text(
                        text = "Ini adalah hasil analisis dari jurnal yang baru Kamu tuliskan",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 16.dp)
                    ){
                        PredictionLabelComponent(
                            label = "Pikiran Mu",
                            prediction = if (model1Prediction == "Suicidal") "Perlu Penanganan" else "Baik baik saja",
                            confidence = model1Confidence ?: "Nan"
                        )
                        PredictionLabelComponent(
                            label = if (model2Prediction == "Normal") "Hebat" else "Kamu Memiliki",
                            prediction = if (model2Prediction == "Normal") "pikiran Positif" else model2Prediction ?: "Error",
                            confidence = model2Confidence ?: "Nan"
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    when {
                        model1Prediction == "Suicidal" -> {
                            // Two buttons for Suicidal prediction
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CustomButtonFilled(
                                    onClick = {  },
                                    text = "Hubungi Layanan",
                                    backgroundColor = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                CustomButtonFilled(
                                    onClick = {  },
                                    text = "Hubungi Kerabat",
                                    backgroundColor = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                CustomButtonFilled(
                                    onClick = onArtikelClick,
                                    text = "Lihat artikel",
                                    backgroundColor = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        else -> {
                            CustomButtonFilled(
                                onClick = onArtikelClick,
                                text = "Lihat artikel",
                                backgroundColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}