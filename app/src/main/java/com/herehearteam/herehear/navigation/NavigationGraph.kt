package com.herehearteam.herehear.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    navController: NavHostController
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
            JournalScreen(navController)
        }
        composable(Screen.Archive.route) {
            ArchiveScreen(navController= navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}