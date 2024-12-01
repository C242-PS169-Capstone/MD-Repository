package com.herehearteam.herehear.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.herehearteam.herehear.data.local.entity.JournalEntity

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournal(journal: JournalEntity)

    @Update
    fun updateJournal(journal: JournalEntity)

    @Delete
    fun deleteJournal(journal: JournalEntity)

    @Query("SELECT * FROM journaling")
    fun getAllJournals(): LiveData<List<JournalEntity>>

//    @Query("SELECT * FROM journaling WHERE journalId = :journalId")
//    fun getJournalByJournalId(journalId: String): JournalEntity?
}