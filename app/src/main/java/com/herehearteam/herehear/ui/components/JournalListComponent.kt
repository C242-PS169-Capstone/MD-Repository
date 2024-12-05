package com.herehearteam.herehear.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun JournalBox(
    text: String,
    color: Color,
    journalId: Int,
    onClick: (Int) -> Unit
){
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(
            topStart = 25.dp,
            topEnd = 25.dp,
            bottomStart = 25.dp,
            bottomEnd = 0.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(102.dp)
            .padding(start = 7.dp)
            .padding(vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center, // Mengatur alignment vertikal
            horizontalAlignment = Alignment.Start // Untuk text align justify
        ) {
            Text(
                text = text,
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Justify,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DateBox(dateTime: LocalDateTime){
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
    val dateFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())

    val day = dateTime.format(dayFormatter) // Nama Hari (String)
    val date = dateTime.format(dateFormatter) // Tanggal (String)

    val dayAbbreviation = day.take(3).uppercase(Locale.getDefault())
    val color = Color(0xFFF4F2F0)

    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 25.dp,
            bottomStart = 0.dp,
            bottomEnd = 25.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(102.dp)
            .padding(end = 7.dp)
            .padding(vertical = 8.dp),
    ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = date,
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                )

                Text(
                    text = dayAbbreviation,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                )
            }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun JournalListPreview(){
    val sampleDateTime = LocalDateTime.now()
    val sampleText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse faucibus lectus tempus, rhoncus nulla vitae, vulputate ligula. Nulla facilisis mollis magna ullamcorper rhoncus. Mauris pretium commo"
    val sampleColor = Color(0xFFFFEEBF)

    Row(
        modifier = Modifier
            .padding(top = 100.dp)
            .padding(end = 16.dp)
    ){
        Box( modifier = Modifier
            .weight(0.22f)
            .fillMaxWidth()){
            DateBox(sampleDateTime)
        }
        Box( modifier = Modifier
            .weight(0.78f)
            .fillMaxWidth()){
            JournalBox(
                text = sampleText,
                color = sampleColor,
                journalId = 0,
                onClick = {}
            )
        }
    }
}