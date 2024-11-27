package com.herehearteam.herehear.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.herehearteam.herehear.data.local.dao.JournalDao
import com.herehearteam.herehear.data.local.entity.JournalEntity

@Database(entities = [JournalEntity::class], version = 1)
abstract class JournalRoomDatabase : RoomDatabase() {
    abstract fun journalDao() : JournalDao

    companion object {
        @Volatile
        private var INSTANCE: JournalRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): JournalRoomDatabase {
            if (INSTANCE == null) {
                synchronized(JournalRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        JournalRoomDatabase::class.java,
                        "journal_database"
                    ).build()
                }
            }
            return INSTANCE as JournalRoomDatabase
        }
    }
}