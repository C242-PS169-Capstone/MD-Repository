package com.herehearteam.herehear.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.herehearteam.herehear.navigation.Screen

object NavigationUtil {
    private val restrictedScreens = listOf(
        Screen.Splash.route,
//        Screen.Login.route,
//        Screen.Register.route,
//        Screen.Settings.route,
//        Screen.Notifications.route
    )

    @Composable
    fun shouldShowBottomBar(navController: NavHostController, splashFinished: Boolean = false): Boolean {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        return !restrictedScreens.contains(currentRoute) && splashFinished
    }
}