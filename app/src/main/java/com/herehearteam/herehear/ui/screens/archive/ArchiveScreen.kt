package com.herehearteam.herehear.ui.screens.archive

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.herehearteam.herehear.ui.components.LabelScreen
import com.herehearteam.herehear.ui.screens.home.HomeScreen
import com.herehearteam.herehear.ui.theme.HereHearTheme

@Composable
fun ArchiveScreen(){
    LabelScreen("Archive")
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HereHearTheme {
        ArchiveScreen()
    }
}