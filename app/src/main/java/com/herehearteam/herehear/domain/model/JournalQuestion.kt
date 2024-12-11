package com.herehearteam.herehear.domain.model

import androidx.compose.ui.graphics.Color

data class JournalQuestion(
    val text: String,
    val backgroundColor: Color
)

object JournalQuestions {
    private val colors = listOf(
        Color(0xFFD1C4FD),
        Color(0xFFBCF1F3),
        Color(0xFFFFEEBF),
        Color(0xFFF3E5DF),
    )

    val questions = listOf(
        JournalQuestion("Apa yang membuatmu merasa bangga hari ini?", colors[0]),
        JournalQuestion("Siapa yang paling membuatmu tersenyum hari ini, dan mengapa?", colors[1]),
        JournalQuestion("Apa satu hal kecil yang membuat harimu lebih baik?", colors[2]),
        JournalQuestion("Apa tantangan terbesar yang kamu hadapi hari ini?", colors[3]),
        JournalQuestion("Bagaimana kamu menunjukkan rasa syukurmu kepada orang lain?", colors[0]),
        JournalQuestion("Apa keputusan terbaik yang kamu buat hari ini?", colors[1]),
        JournalQuestion("Bagaimana kamu menjaga kesehatan fisikmu hari ini?", colors[2]),
        JournalQuestion("Apa momen paling menyenangkan yang kamu alami hari ini?", colors[3]),
        JournalQuestion("Apa satu pelajaran penting yang kamu pelajari hari ini?", colors[0]),
        JournalQuestion("Apa yang kamu harapkan untuk esok hari?", colors[1]),
        JournalQuestion("Apa yang membuatmu merasa damai hari ini?", colors[2]),
        JournalQuestion("Siapa yang kamu hargai dalam hidupmu saat ini, dan mengapa?", colors[3]),
        JournalQuestion("Apa hal baru yang ingin kamu coba minggu ini?", colors[3]),
        JournalQuestion("Apa yang membuatmu merasa produktif hari ini?", colors[2]),
        JournalQuestion("Bagaimana kamu menghadapi rasa stres atau cemas hari ini?", colors[1]),
        JournalQuestion("Apa memori masa kecil yang tiba-tiba teringat hari ini?", colors[0]),
        JournalQuestion("Jika kamu bisa mengulang satu momen hari ini, apa yang akan kamu pilih?", colors[0]),
        JournalQuestion("Bagaimana kamu merayakan pencapaian kecil hari ini?", colors[1]),
        JournalQuestion("Apa yang kamu syukuri tentang dirimu sendiri?", colors[2]),
        JournalQuestion("Apa yang ingin kamu capai dalam waktu dekat?", colors[3]),
        JournalQuestion("Siapa yang ingin kamu hubungi dan mengapa?", colors[0]),
        JournalQuestion("Apa mimpi yang ingin kamu wujudkan?", colors[1]),
        JournalQuestion("Bagaimana kamu menunjukkan rasa cinta kepada dirimu sendiri?", colors[2]),
        JournalQuestion("Apa hal menarik yang kamu baca atau dengar hari ini?", colors[3]),
        JournalQuestion("Jika kamu bisa menggambarkan harimu dengan satu kata, apa itu dan mengapa?", colors[2])
    )


    fun getRandomQuestions(count: Int = 3): List<JournalQuestion> {
        return questions.shuffled().take(count)
    }

    fun getQuestion(text: String): JournalQuestion? {
        return questions.firstOrNull { it.text == text }
    }
}