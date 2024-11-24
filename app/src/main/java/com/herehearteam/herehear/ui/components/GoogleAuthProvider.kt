package com.herehearteam.herehear.ui.components

import androidx.compose.runtime.staticCompositionLocalOf
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient

val LocalGoogleAuthUiClient = staticCompositionLocalOf<GoogleAuthUiClient> {
    error("No GoogleAuthUiClient provided")
}