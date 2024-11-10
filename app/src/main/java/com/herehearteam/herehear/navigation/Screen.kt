package com.herehearteam.herehear.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Article : Screen("article")
    data object Journal : Screen("journal")
    data object Archive : Screen("archive")
    data object Profile : Screen("profile")
}