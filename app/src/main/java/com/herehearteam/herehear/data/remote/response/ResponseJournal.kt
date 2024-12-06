package com.herehearteam.herehear.data.remote.response

import com.google.gson.annotations.SerializedName
import com.herehearteam.herehear.data.model.DataJournal

data class ResponseJournal(
    @field:SerializedName("status")
    val status: Boolean,
    @field:SerializedName("code")
    val code: Int,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: DataJournal,
)
