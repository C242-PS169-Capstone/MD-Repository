package com.herehearteam.herehear.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.herehearteam.herehear.ui.screens.archive.ArchiveScreen
import com.herehearteam.herehear.ui.screens.article.ArticleScreen
import com.herehearteam.herehear.ui.screens.home.HomeScreen
import com.herehearteam.herehear.ui.screens.profile.ProfileScreen
import com.herehearteam.herehear.ui.screens.journal.JournalScreen
import com.herehearteam.herehear.ui.screens.splash.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    onSplashFinished: () -> Unit = {}
){
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                    onSplashFinished()
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
    }
}