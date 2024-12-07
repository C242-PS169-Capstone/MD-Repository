package com.herehearteam.herehear.domain.model

import java.time.LocalDateTime

data class Journal(
    val id: Int,
    val content: String,
    val dateTime: LocalDateTime,
    val question: String?,
    val userId: String,
)
