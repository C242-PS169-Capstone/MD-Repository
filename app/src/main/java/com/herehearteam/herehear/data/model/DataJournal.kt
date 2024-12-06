package com.herehearteam.herehear.data.model

class DataJournal {
    val journalId: String? = null
    val content: String? = null
    val journalClassId: String? = null
    val createdAt: String? = null
}

data class JournalRequestDto(
    val journal_id: String,
    val content: String,
    val journal_class_id: String
)