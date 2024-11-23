package com.herehearteam.herehear.ui.screens.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.domain.model.JournalQuestion
import com.herehearteam.herehear.domain.model.JournalQuestions
import com.herehearteam.herehear.navigation.Screen
import com.herehearteam.herehear.ui.components.CustomButtonOutlined
import com.herehearteam.herehear.ui.components.JournalQuestionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetJournal(
    onDismiss: () -> Unit,
    onSelectQuestion: (JournalQuestion) -> Unit,
    viewModel: JournalViewModel,
    navController: NavHostController
) {
    val currentQuestions by viewModel.currentQuestions.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomButtonOutlined(
                onClick = {
                    viewModel.clearSelectedQuestion()
                    onDismiss()
                    navController.navigate(Screen.Journal.route)
                },
                text = "Jurnal Baru"
            )

            currentQuestions.forEachIndexed { index, question ->
                JournalQuestionCard(
                    question = question,
                    index = index,
                    onClick = { onSelectQuestion(question) },
                    onRefreshClick = { idx -> viewModel.refreshSingleQuestion(idx) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetJournalPreview(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val navController = rememberNavController()

        BottomSheetJournal(
            onDismiss = {},
            onSelectQuestion = {},
            navController = navController,
            viewModel = JournalViewModel()
        )
    }
}