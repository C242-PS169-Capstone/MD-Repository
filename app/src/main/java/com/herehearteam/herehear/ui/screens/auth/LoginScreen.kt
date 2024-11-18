package com.herehearteam.herehear.ui.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herehearteam.herehear.R
import com.herehearteam.herehear.ui.components.CustomButtonFilled
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.theme.ColorPrimary

@Composable
fun LoginScreen(
    onLoginWithGmail: () -> Unit,
    onLoginWithPhone: () -> Unit,
    onNavigateBack: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.BottomCenter
    ){
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            CustomTopAppBar(
                pageTitle = "",
                icon = Icons.Default.ArrowBack,
                onIconClick = onNavigateBack
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .align(Alignment.BottomCenter)
        ) {
            val path = Path().apply {

                moveTo(0f, size.height * 0f)

                cubicTo(
                    x1 = size.width * 0.3f,
                    y1 = size.height * 0.25f,
                    x2 = size.width * 0.75f,
                    y2 = size.height * 0.15f,
                    x3 = size.width,
                    y3 = size.height * 0.35f
                )

                lineTo(size.width, size.height)

                lineTo(0f, size.height)

                close()
            }

            drawPath(
                path = path,
                color = ColorPrimary,
                style = Fill
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 85.dp, horizontal = 16.dp)

        ) {
            CustomButtonFilled(
                text = "Masuk Dengan Gmail",
                backgroundColor = Color.White,
                textColor = Color.Black,
                fontSize = 14.sp,
                icon = painterResource(R.drawable.ic_logo_google),
                onClick = onLoginWithGmail
            )

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.White
                )

                Box(
                    modifier = Modifier.weight(0.2f),
                    contentAlignment = Alignment.Center
                ){
                    Text("or", color = Color.White, fontWeight = FontWeight.Medium)
                }

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(12.dp))

            CustomButtonFilled(
                text = "Masuk Dengan Nomor Telepon",
                backgroundColor = Color.White,
                icon = Icons.Default.Phone,
                textColor = Color.Black,
                fontSize = 14.sp,
                iconColor = Color.Black,
                onClick = onLoginWithPhone,
            )
        }
    }
}

@Preview
@Composable
fun LoginPreview(){
    LoginScreen(
        onLoginWithGmail = { },
        onLoginWithPhone = { },
        onNavigateBack = { }
    )
}