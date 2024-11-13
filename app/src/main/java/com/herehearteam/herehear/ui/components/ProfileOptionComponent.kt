package com.herehearteam.herehear.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileOptionComponent(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.Black
            )
        }

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 56.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileOptionPreview(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        ProfileOptionComponent(
            icon = Icons.Default.Person,
            title = "Kontak Darurat",
            onClick = { }
        )

        Spacer(Modifier.height(10.dp))

        ProfileOptionComponent(
            icon = Icons.Default.Call,
            title = "Kontak Darurat",
            onClick = { }
        )
    }
}
