package com.herehearteam.herehear.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    object Article : Screen("article?filter={filter}"){
        fun createRoute(filter: String? = null): String {
            return if (filter != null) "article?filter=$filter" else "article"
        }
    }
    object Journal : Screen("journal?journalId={journalId}") {
        fun createRoute(journalId: Int? = null): String {
            return if (journalId != null) "journal?journalId=$journalId" else "journal"
        }
    }
    data object Archive : Screen("archive")
    data object Profile : Screen("profile")
    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")
    data object Register : Screen("register")
    data object Login : Screen("login")
    data object Prediction : Screen("prediction")
    data object Term : Screen("term")
}