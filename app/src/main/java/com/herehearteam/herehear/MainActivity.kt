package com.herehearteam.herehear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.navigation.NavigationGraph
import com.herehearteam.herehear.navigation.Screen
import com.herehearteam.herehear.ui.components.BottomNavigationBar
import com.herehearteam.herehear.ui.screens.journal.BottomSheetJournal
import com.herehearteam.herehear.ui.screens.journal.JournalViewModel
import com.herehearteam.herehear.ui.theme.HereHearTheme
import com.herehearteam.herehear.utils.shouldShowBottomBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HereHearTheme {
                AppContent()

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(){
    val navController = rememberNavController()
    val viewModel: JournalViewModel = viewModel()
    val isBottomSheetVisible by viewModel.isBottomSheetVisible.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavigationGraph(navController = navController)

        if (shouldShowBottomBar(navController)) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    NavigationGraph(navController = navController)
                }
            }
        }

        if (isBottomSheetVisible) {
            BottomSheetJournal(
                onDismiss = { viewModel.hideBottomSheet() },
                navController = navController,
                onSelectQuestion = { question ->
                    viewModel.selectQuestion(question)
                    viewModel.hideBottomSheet()
                    navController.navigate(Screen.Journal.route)
                },
                viewModel = viewModel
            )
        }
    }
}
