package com.herehearteam.herehear.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.herehearteam.herehear.R

@Composable
fun UserGreetingCard(
    userName: String,
) {
    val firstName = getFirstName(userName)

    Card(
        modifier = Modifier.height(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD1C4FD)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_greeting),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(45.dp),
                        tint = Color.Unspecified
                    )
                }

            }
            Column(
                modifier = Modifier
                    .weight(15f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hi, $firstName!",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Bagaimana kabarmu hari ini?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
    }
}

fun getFirstName(name: String): String {
    return name.split(" ").firstOrNull() ?: name
}

@Preview(showBackground = true)
@Composable
fun UserGreetingPreview(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        UserGreetingCard(userName = "Arjadikrama")
    }
}