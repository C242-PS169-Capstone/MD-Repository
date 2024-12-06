package com.herehearteam.herehear.ui.screens.journal

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.herehearteam.herehear.ui.components.CustomMemoEditor
import com.herehearteam.herehear.ui.components.CustomTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    journalId: Int?
) {
    val isSaveSuccessful by viewModel.isSaveSuccessful.collectAsState()
    val selectedQuestion by viewModel.selectedQuestion.collectAsState()
    val memoText by viewModel.memoText.collectAsState()
    val isFabExpanded by viewModel.isFabExpanded.collectAsState()
    val shouldShowBackPressedDialog by viewModel.shouldShowBackPressedDialog.collectAsState()
    val shouldShowDeleteConfirmationDialog by viewModel.shouldShowDeleteConfirmationDialog.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(journalId) {
        if (journalId != 0) {
            viewModel.getJournalById(journalId) { journal ->
                journal?.let {
                    viewModel.updateMemoText(it.content) // Set memo text
                    viewModel.selectQuestion(questionObject = null, questionText = it.question) // Set selected question
                }
            }
        }
    }

    BackHandler {
        viewModel.onBackPressed()
    }

    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is NavigationEvent.NavigateBack -> {
                onNavigateBack()
                viewModel.clearNavigationEvent()
            }
            is NavigationEvent.NavigateToHome -> {
                onNavigateToHome()
                viewModel.clearNavigationEvent()
            }
            null -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTopAppBar(
                pageTitle = "Tulis Jurnal",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onIconClick = {
                    viewModel.onBackPressed()
                }
            )

            if (selectedQuestion != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = selectedQuestion?.backgroundColor ?: MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = selectedQuestion?.text ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            CustomMemoEditor(
                value = memoText,
                onValueChange = { viewModel.updateMemoText(it) },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            AnimatedVisibility(
                visible = isFabExpanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                exit = shrinkVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    FloatingActionButton(
                        onClick = { viewModel.saveJournal() },
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = (-8).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Selesai",
                            tint = Color.White
                        )
                    }
                    FloatingActionButton(
                        onClick = { viewModel.showDeleteConfirmationDialog() },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = (-8).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Reset",
                            tint = Color.White
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { viewModel.toggleFabExpanded() },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Crossfade(
                    targetState = isFabExpanded,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) { expanded ->
                    Icon(
                        imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "More Actions",
                        tint = Color.White
                    )
                }
            }
        }

        if (shouldShowBackPressedDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onBackPressedCancel() },
                title = { Text("Keluar Tanpa Menyimpan") },
                text = { Text("Perubahan Anda belum disimpan. Apakah Anda yakin ingin keluar?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onBackPressedConfirm()
                        onNavigateBack()
                    }) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onBackPressedCancel() }) {
                        Text("Batal")
                    }
                }
            )
        }

        if (shouldShowDeleteConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.cancelDeleteConfirmation() },
                title = { Text("Hapus Journal") },
                text = { Text("Apakah kamu yakin ingin menghapus journal ini?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.confirmDelete() }) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.cancelDeleteConfirmation() }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun JournalScreenPreview(){
//    HereHearTheme {
//        JournalScreen(
//            viewModel = JournalViewModel(),
//            onNavigateBack = {}
//        )
//    }
//}