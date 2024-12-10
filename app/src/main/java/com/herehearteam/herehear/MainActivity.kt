package com.herehearteam.herehear

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.domain.repository.UserRepository
import com.herehearteam.herehear.ui.components.LocalGoogleAuthUiClient
import com.herehearteam.herehear.ui.screens.archive.ArchiveViewModel
import com.herehearteam.herehear.ui.screens.archive.ArchiveViewModelFactory
import com.herehearteam.herehear.ui.screens.journal.JournalViewModelFactory
import com.herehearteam.herehear.ui.screens.predict.PredictionViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
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
            val navController = rememberNavController()

            HereHearTheme {
                CompositionLocalProvider(LocalGoogleAuthUiClient provides googleAuthUiClient) {
                    LaunchedEffect(intent) {
                        handleDeepLink(intent, navController)
                    }

                    AppContent(
                        googleAuthUiClient = googleAuthUiClient,
                        navController = navController
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        (findViewById<ComposeView>(android.R.id.content).context as? ComponentActivity)
            ?.let {
                val navController = (it as MainActivity).getNavController()
                handleDeepLink(intent, navController)
            }
    }

    private fun handleDeepLink(intent: Intent, navController: NavController) {
        if (Intent.ACTION_VIEW == intent.action) {
            val deepLink = intent.data
            deepLink?.let {
                when (it.toString()) {
                    "herehear://prediction" -> {
                        navController.navigate(Screen.Prediction.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    private fun getNavController(): NavController {
        return (findViewById<ComposeView>(android.R.id.content).context as MainActivity).navController
    }
}

//class MainActivity : ComponentActivity() {
//    private lateinit var navController: NavController
//
//    private val googleAuthUiClient by lazy {
//        GoogleAuthUiClient(
//            context = applicationContext,
//            oneTapClient = Identity.getSignInClient(applicationContext)
//        )
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        setContent {
//            val navController = rememberNavController()
//
//            HereHearTheme {
//                CompositionLocalProvider(LocalGoogleAuthUiClient provides googleAuthUiClient) {
//                    LaunchedEffect(intent) {
//                        handleIncomingIntent(intent, navController)
//                    }
//
//                    AppContent(
//                        googleAuthUiClient = googleAuthUiClient
//                    )
//                }
//            }
//
//        }
//    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        setIntent(intent)
//
//        navController?.let { controller ->
//            runOnUiThread {
//                val navigateTo = intent.getStringExtra("NAVIGATE_TO")
//                Log.d("MainActivity", "NAVIGATE_TO: $navigateTo")
//
//                when (navigateTo) {
//                    "Prediction" -> {
//                        controller.navigate(Screen.Prediction.route) {
//                            popUpTo(Screen.Splash.route) { inclusive = true }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun handleIncomingIntent(intent: Intent, navController: NavController) {
//        val navigateTo = intent.getStringExtra("NAVIGATE_TO")
//        Log.d("MainActivity", "NAVIGATE_TO: $navigateTo")
//
//        when (navigateTo) {
//            "Prediction" -> {
//                // Pastikan rute "Prediction" ada di NavigationGraph
//                navController.navigate(Screen.Prediction.route) {
//                    popUpTo(Screen.Splash.route) { inclusive = true }
//                }
//            }
//        }
//    }
//}

@Composable
fun AppContent(
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavHostController
){
//    val navController = rememberNavController()
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
