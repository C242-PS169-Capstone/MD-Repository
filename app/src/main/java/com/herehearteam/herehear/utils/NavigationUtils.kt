package com.herehearteam.herehear.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.domain.model.RegisterState
import com.herehearteam.herehear.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val restrictedScreens = listOf(
        Screen.Splash.route,
        Screen.Welcome.route,
        Screen.Login.route,
        Screen.Register.route,
        Screen.InputNumber.route,
        Screen.OtpRegister.route,
        Screen.OtpLogin.route,
        Screen.InputName.route,
        Screen.Term.route,
        Screen.Journal.route,
        Screen.Prediction.route
    )

    return !restrictedScreens.contains(
        navController.currentBackStackEntryAsState().value?.destination?.route
    )
}