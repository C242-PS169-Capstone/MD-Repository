package com.herehearteam.herehear.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.herehearteam.herehear.data.local.entity.EmergencyEntity
import retrofit2.http.GET

@Dao
interface EmergencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergency(emergency: EmergencyEntity)

    @Update
    suspend fun updateEmergency(emergency: EmergencyEntity)

    @Query("SELECT * FROM emergency WHERE userId = :userId")
    suspend fun getEmergencyByUserId(userId: String): EmergencyEntity?
}