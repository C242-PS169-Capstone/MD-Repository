package com.herehearteam.herehear.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "user")
@Parcelize
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "password")
    val password: String
) : Parcelable