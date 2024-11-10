package com.herehearteam.herehear.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.herehearteam.herehear.ui.components.LabelScreen
import com.herehearteam.herehear.ui.theme.HereHearTheme

@Composable
fun ProfileScreen(){
    LabelScreen("Profile")
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HereHearTheme {
        ProfileScreen()
    }
}