package com.herehearteam.herehear.domain.model

import androidx.compose.ui.graphics.Color

data class JournalQuestion(
    val text: String,
    val backgroundColor: Color
)

object JournalQuestions {
    private val colors = listOf(
        Color(0xFFE6F3FF),
        Color(0xFFFFF3E6),
        Color(0xFFE6FFE6),
        Color(0xFFFFE6E6),
        Color(0xFFF3E6FF)
    )

    val questions = listOf(
        JournalQuestion("What was the best part of your day?", colors[0]),
        JournalQuestion("What are you grateful for today?", colors[1]),
        JournalQuestion("What's one thing you'd like to improve?", colors[2]),
        JournalQuestion("What challenges did you face today?", colors[3]),
        JournalQuestion("How did you take care of yourself today?", colors[4]),
    )

    fun getRandomQuestions(count: Int = 3): List<JournalQuestion> {
        return questions.shuffled().take(count)
    }
}