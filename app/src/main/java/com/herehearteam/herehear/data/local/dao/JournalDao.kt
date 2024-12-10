package com.herehearteam.herehear.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.herehearteam.herehear.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournal(journal: JournalEntity)

    @Update
    fun updateJournal(journal: JournalEntity)

    @Delete
    fun deleteJournal(journal: JournalEntity)

    @Query("SELECT * FROM journaling WHERE userId = :userId")
    fun getAllJournalsFlow(userId: String): Flow<List<JournalEntity>>

    @Query("SELECT * FROM journaling WHERE journalId = :journalId AND userId = :userId")
    fun getJournalByJournalId(journalId: Int, userId: String): JournalEntity?

    @Query("DELETE FROM journaling WHERE journalId = :journalId AND userId = :userId")
    fun deleteJournalById(journalId: Int, userId: String)

    @Query("UPDATE journaling SET content = :content WHERE journalId = :journalId AND userId = :userId")
    fun updateJournalContentById(journalId: Int, content: String, userId: String)

    @Query("DELETE FROM journaling WHERE userId = :userId")
    fun deleteAllJournalsForUser(userId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournals(journals: List<JournalEntity>)

    @Query("SELECT * FROM journaling WHERE isSync = 0")
    fun getUnsyncedJournals(): List<JournalEntity>

    @Query("SELECT * FROM journaling WHERE isPredicted = 0")
    fun getPendingPredictionJournals(): List<JournalEntity>

    // New method to mark a journal as synced
    @Query("UPDATE journaling SET isSync = 1 WHERE journalId = :journalId")
    fun markJournalAsSynced(journalId: Int)

    @Query("SELECT * FROM journaling WHERE userId = :userId AND isPredicted = 1 ORDER BY createdDate DESC LIMIT 1")
    fun getLastPredictedJournal(userId: String): JournalEntity?

    @Query("""
    UPDATE journaling 
    SET predict1Label = :predict1Label, 
        predict1Confidence = :predict1Confidence, 
        predict2Label = :predict2Label, 
        predict2Confidence = :predict2Confidence, 
        isPredicted = :isPredicted 
    WHERE journalId = :journalId AND userId = :userId
""")
    fun updateJournalPredictionsByJournalAndUserId(
        journalId: Int,
        userId: String,
        predict1Label: String?,
        predict1Confidence: String?,
        predict2Label: String?,
        predict2Confidence: String?,
        isPredicted: Boolean
    )
}