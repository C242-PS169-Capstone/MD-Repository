package com.herehearteam.herehear.navigation

import androidx.compose.runtime.Composable
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
import com.herehearteam.herehear.ui.screens.auth.RegisterScreen
import com.herehearteam.herehear.ui.screens.auth.WelcomeScreen
import com.herehearteam.herehear.ui.screens.home.HomeScreen
import com.herehearteam.herehear.ui.screens.journal.JournalScreen
import com.herehearteam.herehear.ui.screens.profile.ProfileScreen
import com.herehearteam.herehear.ui.screens.splash.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController
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
            JournalScreen()
        }
        composable(Screen.Archive.route) {
            ArchiveScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
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
                }
            )
        }
        composable(
            Screen.InputNumber.route,
            arguments = listOf(navArgument("isRegister") { type = NavType.BoolType})
        ) {
            InputNumberScreen()
        }
//        composable(Screen.OtpRegister.route) {
//            OtpLoginScreen()
//        }
//        composable(Screen.OtpLogin.route) {
//            OtpRegisterScreen()
//        }
        composable(Screen.InputName.route) {
            NameInputScreen()
        }
//        composable(Screen.Term.route) {
//            TermsAndConditionsScreen()
//        }
//        composable(Screen.Welcome.route) {
//            WelcomeScreen(
//                navigateToLogin = { navController.navigate(Screen.Login.route) },
//                navigateToRegister = { navController.navigate(Screen.Register.route) }
//            )
//        }

//        composable(Screen.InputNumber.route, arguments = listOf(
//            navArgument("isRegister") { type = NavType.BoolType }
//        )) { backStackEntry ->
//            val isRegister = backStackEntry.arguments?.getBoolean("isRegister") ?: false
//            InputNumberScreen(
//                onOtpRequested = { phoneNumber ->
//                    if (isRegister) {
//                        navController.navigate(Screen.OtpRegister.createRoute(phoneNumber))
//                    } else {
//                        navController.navigate(Screen.OtpLogin.createRoute(phoneNumber))
//                    }
//                }
//            )
//        }
//        composable(Screen.OtpRegister.route, arguments = listOf(
//            navArgument("phoneNumber") { type = NavType.StringType }
//        )) { backStackEntry ->
//            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber").orEmpty()
//            OtpRegisterScreen(
//                phoneNumber = phoneNumber,
//                onVerified = {
//                    navController.navigate(Screen.InputName.createRoute(phoneNumber)) {
//                        popUpTo(Screen.OtpRegister.route) { inclusive = true }
//                    }
//                }
//            )
//        }
//        composable(Screen.OtpLogin.route, arguments = listOf(
//            navArgument("phoneNumber") { type = NavType.StringType }
//        )) { backStackEntry ->
//            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber").orEmpty()
//            OtpLoginScreen(
//                phoneNumber = phoneNumber,
//                onVerified = {
//                    navController.navigate(Screen.Home.route) {
//                        popUpTo(Screen.OtpLogin.route) { inclusive = true }
//                    }
//                }
//            )
//        }
//        composable(Screen.InputName.route, arguments = listOf(
//            navArgument("phoneNumber") { type = NavType.StringType }
//        )) { backStackEntry ->
//            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber").orEmpty()
//            NameInputScreen(
//                onTermsAccepted = {
//                    navController.navigate(Screen.Term.route) {
//                        popUpTo(Screen.InputName.route) { inclusive = true }
//                    }
//                }
//            )
//        }
//        composable(Screen.Term.route) {
//            TermsAndConditionsScreen(
//                onAccepted = {
//                    navController.navigate(Screen.Home.route) {
//                        popUpTo(Screen.Term.route) { inclusive = true }
//                    }
//                }
//            )
//        }
    }
}