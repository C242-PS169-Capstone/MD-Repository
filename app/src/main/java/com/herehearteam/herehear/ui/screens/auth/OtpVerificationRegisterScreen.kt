package com.herehearteam.herehear.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herehearteam.herehear.ui.components.CustomButtonFilled
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.theme.ColorPrimary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpRegisterScreen(phoneNumber: String){
    var otp by remember { mutableStateOf(Array(6) { "" }) }
    var countdown by remember { mutableStateOf(120) }
    val minutes = countdown / 60
    val seconds = countdown % 60

    LaunchedEffect(countdown) {
        if(countdown > 0){
            delay(1000L)
            countdown -= 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        CustomTopAppBar(
            pageTitle = "OTP",
            icon = Icons.AutoMirrored.Filled.ArrowBack
        )

        Text(
            text = "Periksa nomor telepon kamu",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Text(
            text = "Masukan 6 digit kode verifikasi",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )

        Text(
            text = "Kode telah dikiramkan ke (+62) $phoneNumber",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(6) { index ->
                TextField(
                    value = otp[index],
                    onValueChange = {
                        if (it.length <= 1) {
                            otp[index] = it
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = ColorPrimary,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
            }
        }

        CustomButtonFilled(
            onClick = { },
            text = "Lanjut",
            backgroundColor = ColorPrimary
        )

        Text(
            text = "Waktu yang tersisa untuk memasukan kode ${"%02d:%02d".format(minutes, seconds)}",
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (countdown == 0) {
            Text(
                text = "Kirim ulang kode",
                color = ColorPrimary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
                    .clickable {
                        countdown = 120
                    }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun OtpRegisterPreview(){
    OtpRegisterScreen(phoneNumber = "88225342861")
}