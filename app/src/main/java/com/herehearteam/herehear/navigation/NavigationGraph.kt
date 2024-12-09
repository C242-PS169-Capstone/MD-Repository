package com.herehearteam.herehear.navigation

import RegisterViewModel
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.local.repository.PredictionRepository
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.domain.model.SignInResult
import com.herehearteam.herehear.ui.screens.archive.ArchiveScreen
import com.herehearteam.herehear.ui.screens.archive.ArchiveViewModel
import com.herehearteam.herehear.ui.screens.article.ArticleScreen
import com.herehearteam.herehear.ui.screens.auth.InputNumberScreen
import com.herehearteam.herehear.ui.screens.auth.LoginScreen
import com.herehearteam.herehear.ui.screens.auth.LoginViewModel
import com.herehearteam.herehear.ui.screens.auth.LoginViewModelFactory
import com.herehearteam.herehear.ui.screens.auth.NameInputScreen
import com.herehearteam.herehear.ui.screens.auth.OtpLoginScreen
import com.herehearteam.herehear.ui.screens.auth.OtpRegisterScreen
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
import com.herehearteam.herehear.ui.screens.predict.PredictionViewModelFactory
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

    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(
            appDependencies.userRepository,
            googleAuthUiClient
        )
    )

    val registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(
            appDependencies.userRepository
        )
    )

    val apiService = ApiConfig.getApiService()
    val predictionViewModel: PredictionViewModel = viewModel(
        factory = PredictionViewModelFactory(
            PredictionRepository(apiService),
            appDependencies.userRepository)
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
                }
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

        composable(Screen.Article.route) {
            ArticleScreen(
                onNavigateBack = { navController.popBackStack() }
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
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(googleAuthUiClient)
            )
            val context = LocalContext.current
            var showToast by remember { mutableStateOf(false) }

            LaunchedEffect(showToast) {
                if (showToast) {
                    Toast.makeText(context, "Jurnal berhasil disimpan!", Toast.LENGTH_SHORT).show()
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
                }
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

        composable(
            Screen.InputNumber.route,
            arguments = listOf(navArgument("isRegister") { type = NavType.BoolType})
        ) { backStackEntry ->
            val isRegister = backStackEntry.arguments?.getBoolean("isRegister") ?: false
            InputNumberScreen(
                onNavigateToOtpWhatsApp = { phoneNumber ->
                    if (isRegister) {
                        navController.navigate(Screen.OtpRegister.createRoute(phoneNumber))
                    } else {
                        navController.navigate(Screen.OtpLogin.createRoute(phoneNumber))
                    }
                },
                onNavigateToOtpSms = { phoneNumber ->
                    if (isRegister) {
                        navController.navigate(Screen.OtpRegister.createRoute(phoneNumber))
                    } else {
                        navController.navigate(Screen.OtpLogin.createRoute(phoneNumber))
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.OtpRegister.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber").orEmpty()
            OtpRegisterScreen(
                phoneNumber = phoneNumber,
                onNavigateToNameInput = {
                    navController.navigate(Screen.InputName.createRoute(phoneNumber))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.OtpLogin.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber").orEmpty()
            OtpLoginScreen(
                phoneNumber = phoneNumber,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.InputName.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber").orEmpty()
            NameInputScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToTermsAndConditions = {
                    navController.navigate(Screen.Term.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
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