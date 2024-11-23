package com.herehearteam.herehear.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.herehearteam.herehear.ui.screens.archive.ArchiveScreen
import com.herehearteam.herehear.ui.screens.article.ArticleScreen
import com.herehearteam.herehear.ui.screens.auth.InputNumberScreen
import com.herehearteam.herehear.ui.screens.auth.LoginScreen
import com.herehearteam.herehear.ui.screens.auth.NameInputScreen
import com.herehearteam.herehear.ui.screens.auth.OtpLoginScreen
import com.herehearteam.herehear.ui.screens.auth.OtpRegisterScreen
import com.herehearteam.herehear.ui.screens.auth.RegisterScreen
import com.herehearteam.herehear.ui.screens.auth.TermsAndConditionsScreen
import com.herehearteam.herehear.ui.screens.auth.WelcomeScreen
import com.herehearteam.herehear.ui.screens.home.HomeScreen
import com.herehearteam.herehear.ui.screens.journal.JournalScreen
import com.herehearteam.herehear.ui.screens.journal.JournalViewModel
import com.herehearteam.herehear.ui.screens.profile.ProfileScreen
import com.herehearteam.herehear.ui.screens.splash.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: JournalViewModel = viewModel()
){
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Article.route) {
            ArticleScreen()
        }

        composable(Screen.Journal.route) {
            JournalScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Archive.route) {
            ArchiveScreen(navController= navController)
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
            RegisterScreen(
                onRegisterWithGmail = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onRegisterWithPhone = {
                    navController.navigate(Screen.InputNumber.createRoute(isRegister = true))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginWithGmail = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginWithPhone = {
                    navController.navigate(Screen.InputNumber.createRoute(isRegister = false))
                },
                onNavigateBack = {
                    navController.popBackStack()
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