package com.herehearteam.herehear.ui.screens.article

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.herehearteam.herehear.ui.components.LabelScreen
import com.herehearteam.herehear.ui.theme.HereHearTheme

@Composable
fun ArticleScreen(){
    LabelScreen("Article")
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HereHearTheme {
        ArticleScreen()
    }
}