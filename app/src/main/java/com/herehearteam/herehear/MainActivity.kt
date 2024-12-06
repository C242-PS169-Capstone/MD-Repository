package com.herehearteam.herehear

import android.app.Application
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.herehearteam.herehear.navigation.NavigationGraph
import com.herehearteam.herehear.navigation.Screen
import com.herehearteam.herehear.ui.components.BottomNavigationBar
import com.herehearteam.herehear.ui.screens.journal.BottomSheetJournal
import com.herehearteam.herehear.ui.screens.journal.JournalViewModel
import com.herehearteam.herehear.ui.theme.HereHearTheme
import com.herehearteam.herehear.utils.shouldShowBottomBar
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.ui.components.LocalGoogleAuthUiClient
import com.herehearteam.herehear.ui.screens.archive.ArchiveViewModel
import com.herehearteam.herehear.ui.screens.archive.ArchiveViewModelFactory
import com.herehearteam.herehear.ui.screens.journal.JournalViewModelFactory

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HereHearTheme {
                CompositionLocalProvider(LocalGoogleAuthUiClient provides googleAuthUiClient) {
                    AppContent(
                        googleAuthUiClient = googleAuthUiClient
                    )
                }
            }
        }
    }
}

@Composable
fun AppContent(
    googleAuthUiClient: GoogleAuthUiClient
){
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as Application
    val viewModel: JournalViewModel = viewModel(
        factory = JournalViewModelFactory.getInstance(application = application)
    )
    val isBottomSheetVisible by viewModel.isBottomSheetVisible.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavigationGraph(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
        )

        if (shouldShowBottomBar(navController)) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        navController = navController
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    NavigationGraph(
                        navController = navController,
                        googleAuthUiClient = googleAuthUiClient,
                    )
                }
            }
        }

        if (isBottomSheetVisible) {
            BottomSheetJournal(
                onDismiss = { viewModel.hideBottomSheet() },
                navController = navController,
                onSelectQuestion = { question ->
                    viewModel.selectQuestion(question, null)
                    viewModel.hideBottomSheet()
                    navController.navigate(Screen.Journal.route)
                },
                viewModel = viewModel
            )
        }
    }
}
