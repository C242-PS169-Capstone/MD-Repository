package com.herehearteam.herehear.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "emergency")
@Parcelize
data class EmergencyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "namaKontak")
    val namaKontak: String?,

    @ColumnInfo(name = "nomorTelepon")
    val nomorTelepon: String?,

    @ColumnInfo(name = "hubungan")
    val hubungan: String?
) : Parcelable