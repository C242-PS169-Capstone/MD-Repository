package com.herehearteam.herehear.domain.model

data class JournalState(
    val isSaveSucces: Boolean = false,
    val saveError: String? = null,
    val isLoading: Boolean = false
)