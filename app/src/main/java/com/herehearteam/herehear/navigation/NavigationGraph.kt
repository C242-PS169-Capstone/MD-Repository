package com.herehearteam.herehear.navigation

import RegisterViewModel
import android.app.Activity.RESULT_OK
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.herehearteam.herehear.data.local.repository.JournalRepository
import com.herehearteam.herehear.data.local.repository.PredictionRepository
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.domain.model.SignInResult
import com.herehearteam.herehear.ui.screens.archive.ArchiveScreen
import com.herehearteam.herehear.ui.screens.article.ArticleScreen
import com.herehearteam.herehear.ui.screens.article.ArticleViewModelFactory

import com.herehearteam.herehear.ui.screens.auth.LoginScreen
import com.herehearteam.herehear.ui.screens.auth.LoginViewModel
import com.herehearteam.herehear.ui.screens.auth.LoginViewModelFactory

import com.herehearteam.herehear.ui.screens.auth.RegisterScreen
import com.herehearteam.herehear.ui.screens.auth.RegisterViewModelFactory
import com.herehearteam.herehear.ui.screens.auth.TermsAndConditionsScreen
import com.herehearteam.herehear.ui.screens.auth.WelcomeScreen
import com.herehearteam.herehear.ui.screens.home.HomeScreen
import com.herehearteam.herehear.ui.screens.home.HomeViewModel
import com.herehearteam.herehear.ui.screens.home.HomeViewModelFactory
import com.herehearteam.herehear.ui.screens.journal.JournalScreen
import com.herehearteam.herehear.ui.screens.journal.JournalViewModel
import com.herehearteam.herehear.ui.screens.journal.JournalViewModelFactory
import com.herehearteam.herehear.ui.screens.predict.PredictScreen
import com.herehearteam.herehear.ui.screens.predict.PredictionViewModel
import com.herehearteam.herehear.ui.screens.profile.ProfileScreen
import com.herehearteam.herehear.ui.screens.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient
){

  
    val application = LocalContext.current.applicationContext as Application
    val viewModel: JournalViewModel = viewModel(
        factory = JournalViewModelFactory.getInstance(application = application)
    )
    val context = LocalContext.current
    val appDependencies = AppDependencies.getInstance(context)
    val journalRepository = JournalRepository(application)


    val apiService = ApiConfig.getApiService()
    val predictionRepository = PredictionRepository(apiService)
    val predictionViewModel = PredictionViewModel(predictionRepository)

    val apiArticleService = ApiConfig.getArticleService();
    val articleViewModel = ArticleViewModelFactory(apiArticleService)
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(
            appDependencies.userRepository,
            googleAuthUiClient
        )
    )

    val registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(
            apiService,
            appDependencies.userRepository
        )
    )
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Prediction.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "herehear://prediction"
                }
            )
        ) {
            PredictScreen(
                onBackClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                journalRepository = journalRepository,
                navController = navController
            )
        }

        composable(Screen.Splash.route) {
            SplashScreen(
                navigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(
            Screen.Article.route,
            arguments = listOf(
                navArgument("filter") {
                    type = NavType.StringType
                    defaultValue = "Anxiety"
                })
            ) {
                backStackEntry ->
            val filters = listOf(
                "Anxiety",
                "Depression",
                "Suicidal",
                "Stress",
                "Bipolar",
                "Personality disorder"
            )
            val initialFilter = backStackEntry.arguments?.getString("filter") ?: filters.first()
            Log.d("GUAA", "ini adalah $initialFilter")
            val selectedInitialFilter = filters.find { it.equals(initialFilter, ignoreCase = true) }
                ?: filters.first()
            Log.d("GUAA", "ini adalakhgouh $selectedInitialFilter")
            ArticleScreen(
                onNavigateBack = { navController.popBackStack() },
                initialFilter = selectedInitialFilter
            )
        }

        composable(Screen.Journal.route,
            arguments = listOf(
                navArgument("journalId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )) { backStackEntry ->
            val journalId = backStackEntry.arguments?.getInt("journalId") ?: 0
            var showToast by remember { mutableStateOf(false) }

            LaunchedEffect(showToast) {
                if (showToast) {
                    Toast.makeText(context, "berhasil", Toast.LENGTH_SHORT).show()
                    showToast = false
                }
            }

            JournalScreen(
                viewModel = viewModel,
                journalId = journalId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.popBackStack()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    showToast = true
                },

            )
        }

        composable(Screen.Archive.route) {
            ArchiveScreen(
                navController= navController,
                application = LocalContext.current.applicationContext as Application)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                navigateToLogin = { navController.navigate(Screen.Login.route) },
                navigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            val state by registerViewModel.state.collectAsState()
            var signInResult by remember { mutableStateOf<SignInResult?>(null) }

            Log.d("RegisterScreen", "Registration Error: ${state.isRegistrationSuccessful}")

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        scope.launch {
                            val result = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            // Update signInResult
                            signInResult = result
                            loginViewModel.onSignInResult(result)
                            registerViewModel.onSignInResult(result)
                        }
                    }
                }
            )

            LaunchedEffect(signInResult) {
                signInResult?.let { result ->
                    handleSignInResult(
                        result = result,
                        onSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Welcome.route) { inclusive = true }
                            }
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            RegisterScreen(
                onRegisterWithGmail = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route)
                },
                apiService = apiService
            )
        }

        composable(Screen.Login.route) {
            val state by loginViewModel.state.collectAsState()
            var signInResult by remember { mutableStateOf<SignInResult?>(null) }
            Log.d("LoginScreen", "login status anjing: ${state.isSignInSuccessful}")
            Log.d("LoginScreen", "User: ${state.currentUser}")

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        scope.launch {
                            val result = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            // Update signInResult
                            signInResult = result
                            registerViewModel.onSignInResult(result)
                            loginViewModel.onSignInResult(result)
                        }
                    }
                }
            )

            LaunchedEffect(signInResult) {
                signInResult?.let { result ->
                    handleSignInResult(
                        result = result,
                        onSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Welcome.route) { inclusive = true }
                            }
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            LoginScreen(
                onLoginWithGmail = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Term.route) {
            TermsAndConditionsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}

fun handleSignInResult(
    result: SignInResult,
    onSuccess: () -> Unit,
    onError: (String?) -> Unit
) {
    if (result.data != null) {
        onSuccess()
    } else {
        onError(result.errorMessage)
    }
}