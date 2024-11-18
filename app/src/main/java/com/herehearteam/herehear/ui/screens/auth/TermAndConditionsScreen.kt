package com.herehearteam.herehear.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.ui.components.CustomTopAppBar

data class Term(
    val title: String,
    val content: String
)

val termsList = listOf(
    Term(
        title = "1. Penggunaan Aplikasi",
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla facilisi. Etiam scelerisque nisl sed arcu bibendum, ut sollicitudin orci luctus et ultrices posuere cubilia curae."
    ),
    Term(
        title = "2. Keamanan dan Privasi Data",
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus laoreet, magna nec commodo dapibus, libero risus fermentum augue, ac finibus nisi tortor sed erat. Phasellus tincidunt purus eget risus feugiat, quis commodo purus lobortis."
    ),
        Term(
        title = "3. Konsultasi dan Keterbatasan",
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla facilisi. Etiam scelerisque nisl sed arcu bibendum, ut sollicitudin orci luctus et ultrices posuere cubilia curae."
    ),
    Term(
        title = "4. Hak Kekayaan Intelektual",
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus laoreet, magna nec commodo dapibus, libero risus fermentum augue, ac finibus nisi tortor sed erat. Phasellus tincidunt purus eget risus feugiat, quis commodo purus lobortis."
    ),
        Term(
        title = "5. Perubahan Ketentuan",
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla facilisi. Etiam scelerisque nisl sed arcu bibendum, ut sollicitudin orci luctus et ultrices posuere cubilia curae."
    ),
    Term(
        title = "6. Batasan Tanggung Jawab",
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus laoreet, magna nec commodo dapibus, libero risus fermentum augue, ac finibus nisi tortor sed erat. Phasellus tincidunt purus eget risus feugiat, quis commodo purus lobortis."
    ),


)


@Composable
fun TermsAndConditionsScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CustomTopAppBar(
            pageTitle = "Syarat dan Ketentuan",
            icon = Icons.Default.ArrowBack,
            onIconClick = onNavigateBack
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Syarat dan Ketentuan Penggunaan HereHear",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        termsList.forEach { term ->
            Text(
                text = term.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = term.content,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAndConditionsScreenPreview() {
    TermsAndConditionsScreen(
        onNavigateBack = {}
    )
}