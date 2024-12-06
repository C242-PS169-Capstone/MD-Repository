package com.herehearteam.herehear.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Article : Screen("article")
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
    data object InputNumber : Screen("input_number/{isRegister}") {
        fun createRoute(isRegister: Boolean) = "input_number/$isRegister"
    }
    data object OtpRegister : Screen("otp_register/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp_register/$phoneNumber"
    }
    data object OtpLogin : Screen("otp_login/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp_login/$phoneNumber"
    }
    data object InputName : Screen("input_name/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "input_name/$phoneNumber"
    }
    data object Term : Screen("term")
}