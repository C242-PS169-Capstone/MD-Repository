package com.herehearteam.herehear.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.herehearteam.herehear.navigation.Screen

@Composable
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val restrictedScreens = listOf(
        Screen.Splash.route,
        Screen.Welcome.route,
        Screen.Login.route,
        Screen.Register.route,
        Screen.InputNumber.route,
//        Screen.Settings.route,
//        Screen.Notifications.route
    )

    return !restrictedScreens.contains(
        navController.currentBackStackEntryAsState().value?.destination?.route
    )
}