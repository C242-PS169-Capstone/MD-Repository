package com.herehearteam.herehear.ui.screens.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.herehearteam.herehear.domain.model.JournalQuestion
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
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
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
                    navController.navigate(Screen.Journal.createRoute())
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
