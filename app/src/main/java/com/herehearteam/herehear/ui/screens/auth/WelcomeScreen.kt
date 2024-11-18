package com.herehearteam.herehear.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herehearteam.herehear.R
import com.herehearteam.herehear.ui.components.CustomButtonFilled
import com.herehearteam.herehear.ui.components.CustomButtonOutlined

@Composable
fun WelcomeScreen(
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 85.dp, horizontal = 16.dp)

        ) {
            CustomButtonFilled(
                onClick = navigateToLogin,
                text = stringResource(R.string.masuk),
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(16.dp))

            CustomButtonOutlined(
                onClick = navigateToRegister,
                text = stringResource(R.string.daftar),
                letterSpacing = 2.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview(){
    WelcomeScreen(
        navigateToLogin = {},
        navigateToRegister = {}
    )
}