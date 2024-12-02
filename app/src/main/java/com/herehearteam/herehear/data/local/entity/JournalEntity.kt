package com.herehearteam.herehear.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "journaling")
@Parcelize
data class JournalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "journalId")
    val journalId: Int = 0,

    @ColumnInfo(name = "createdDate")
    val createdDate: String? = null,

    @ColumnInfo(name = "content")
    val content: String? = null,

//    @ColumnInfo(name = "userId")
//    val userId: String,

//    @ColumnInfo(name = "journalingClassName")
//    val journalingClassName: String,

    @ColumnInfo(name = "question")
    val question: String?
) : Parcelable
