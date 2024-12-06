package com.herehearteam.herehear.data.local.helper

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

object JournalHelper {
    fun getCurrentDate(): LocalDateTime {
        return LocalDateTime.now()
    }

}