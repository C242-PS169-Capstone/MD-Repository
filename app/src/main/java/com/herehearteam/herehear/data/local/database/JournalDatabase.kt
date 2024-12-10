package com.herehearteam.herehear.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.herehearteam.herehear.data.local.dao.JournalDao
import com.herehearteam.herehear.data.local.entity.JournalEntity
import androidx.room.TypeConverters
import com.herehearteam.herehear.data.local.dao.EmergencyDao
import com.herehearteam.herehear.data.local.entity.EmergencyEntity
import com.herehearteam.herehear.data.local.helper.Converters


@Database(entities = [JournalEntity::class, EmergencyEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalDao() : JournalDao
    abstract fun emergencyDao() : EmergencyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "journal_database"
                    ).build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}