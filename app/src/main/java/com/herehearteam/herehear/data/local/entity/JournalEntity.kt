package com.herehearteam.herehear.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "journaling")
@Parcelize
data class JournalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "journalId")
    val journalId: Int = 0,

    @ColumnInfo(name = "createdDate")
    val createdDate: LocalDateTime,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "question")
    val question: String?,

    @ColumnInfo(name = "isSync")
    val isSync: Boolean = false
) : Parcelable
