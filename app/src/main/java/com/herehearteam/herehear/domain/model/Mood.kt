package com.herehearteam.herehear.domain.model

import java.time.LocalDate

data class MoodEmoji(
    val id: Int,
    val emoji: String,
    val description: String
)

data class DayMood(
    val day: String,
    val mood: MoodEmoji?,
    val date: LocalDate
)