package com.herehearteam.herehear.data.model

import com.google.gson.annotations.SerializedName

data class DataJournal (
    @SerializedName("journal_id") val journalId: Int,
    val content: String,
    @SerializedName("created_date") val createdDate: String,
    @SerializedName("user_id") val userId: String,
    val question: String
)

data class JournalRequestDto(
    val content: String,
    val user_id: String,
    val question: String?
)

data class JournalUpdateRequestDto(
    val content: String?,
    val question: String?

)