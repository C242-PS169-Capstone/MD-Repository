package com.herehearteam.herehear.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.ui.components.LabelScreen
import com.herehearteam.herehear.ui.theme.HereHearTheme

@Composable
fun HomeScreen(navController: NavHostController) {
    LabelScreen("Home")
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HereHearTheme {
        val navController = rememberNavController()

        HomeScreen(navController)
    }
}