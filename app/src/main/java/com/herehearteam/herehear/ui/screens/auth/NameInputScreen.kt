package com.herehearteam.herehear.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herehearteam.herehear.ui.components.CustomButtonFilled
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.theme.ColorPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInputScreen() {
    var name by remember { mutableStateOf("") }
    var isTermsChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        CustomTopAppBar(
            pageTitle = "Nama",
            icon = Icons.Default.ArrowBack
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bagaimana kami bisa menyapamu",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = ColorPrimary,
                unfocusedIndicatorColor = Color.Gray
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = isTermsChecked,
                onCheckedChange = { isTermsChecked = it },
                colors = CheckboxDefaults.colors(ColorPrimary),
            )

            Text(
                text = "Saya setuju dengan ",
                fontSize = 14.sp
            )

            Text(
                text = "Syarat dan Ketentuan",
                fontSize = 14.sp,
                color = ColorPrimary,
                modifier = Modifier.clickable {
//                    navController.navigate("terms_and_conditions")
                }
            )
        }

        CustomButtonFilled(
            onClick = { },
            text = "Lanjut",
        )

        Text(
            text = "Dengan melanjutkan, kamu setuju dengan Syarat dan Ketentuan HereHear",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NameInputScreenPreview() {
    NameInputScreen()
}
