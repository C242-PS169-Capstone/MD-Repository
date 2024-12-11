package com.herehearteam.herehear.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text as Text1

@Composable
fun DailyPositiveQuoteCard(
    quote: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEEBF)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text1(
                text = "Kata Positif Hari Ini",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp),
                color = Color.Black
            )
            Text1(
                text = "\"$quote\"",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp),
                color = Color.Black
            )
            Text1(
                text = "- HereHear",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontStyle = FontStyle.Italic
                ),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.DarkGray
            )
        }
    }
}

fun getDailyPositiveQuote(): String {
    val quotes = listOf(
        "Kami percaya, kamu lebih kuat dari yang kamu pikirkan.",
        "Hari ini adalah milikmu untuk menciptakan sesuatu yang indah, dan kami mendukungmu.",
        "Ingat, kamu tidak sendirian. Kami di sini untuk menemanimu.",
        "Kami yakin, kamu bisa menghadapi segala tantangan dengan penuh keberanian.",
        "Luangkan waktu untuk dirimu sendiri, kamu layak mendapatkan itu.",
        "Setiap langkah kecil yang kamu ambil adalah pencapaian besar bagi kami.",
        "Kami percaya, ada kekuatan besar dalam dirimu yang menunggu untuk bersinar.",
        "Jangan lupa, kamu adalah sumber inspirasi bagi orang-orang di sekitarmu, termasuk kami.",
        "Kami di sini untuk mengingatkanmu bahwa kamu pantas untuk bahagia.",
        "Kegagalan adalah bagian dari prosesmu menuju kesuksesan. Kami tahu kamu bisa.",
        "Kami bangga padamu untuk semua hal baik yang telah kamu capai.",
        "Jika harimu terasa berat, ingatlah bahwa kami percaya kamu bisa melewatinya.",
        "Setiap detik adalah kesempatan baru untuk menjadi versi terbaik dirimu, dan kami mendukungmu.",
        "Kami percaya, kebahagiaanmu dimulai dari langkah kecil yang kamu ambil hari ini.",
        "Ingat, kamu unik dan itulah yang membuatmu luar biasa.",
        "Kami tahu, kamu punya kekuatan untuk membuat hari ini lebih baik.",
        "Kamu istimewa, dan kami di sini untuk memastikan kamu tahu itu.",
        "Kami ingin kamu tahu bahwa kamu dicintai dan dihargai.",
        "Jangan menyerah, kami yakin kamu memiliki potensi luar biasa.",
        "Hari ini adalah hadiah, dan kami ingin kamu menikmatinya sepenuh hati.",
        "Kamu telah melalui banyak hal, dan kami bangga melihatmu terus maju.",
        "Kami percaya, kesulitan hari ini adalah pelajaran untuk kekuatanmu di masa depan.",
        "Teruslah mencoba, kami ada di sini untuk mendukung setiap langkahmu.",
        "Kamu layak mendapatkan yang terbaik, dan kami ingin kamu merasakannya.",
        "Jika kamu butuh jeda, ambillah. Kami ingin kamu tahu bahwa itu tidak apa-apa.",
        "Kami yakin, dunia menjadi tempat yang lebih baik karena kehadiranmu.",
        "Setiap momen adalah kesempatan baru, dan kami ingin kamu memanfaatkannya sebaik mungkin.",
        "Kami percaya, ada hal baik yang menunggu untuk datang ke dalam hidupmu.",
        "Luangkan waktu untuk tersenyum hari ini, kami yakin itu akan membuatmu merasa lebih baik.",
        "Kami di sini untuk mengingatkanmu bahwa kamu luar biasa, hari ini dan setiap hari."
    )
    return quotes.random()
}
