package com.nargok.timeclock.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nargok.timeclock.data.db.entity.EffortEntity

@Dao
interface EffortDao {
    @Query("SELECT * FROM effort WHERE date >= :fromDate AND date <= :toDate ORDER BY id DESC")
    fun getEfforts(fromDate: String, toDate: String): List<EffortEntity>

    @Query("SELECT * FROM effort WHERE id = :id")
    fun findEffortById(id: String): EffortEntity?

    @Query("SELECT * FROM effort WHERE date = :date")
    fun findEffortByDate(date: String): EffortEntity?

    @Insert
    suspend fun insertEffort(effort: EffortEntity)

    @Query("UPDATE effort SET date = :date, start_time = :startTime, end_time = :endTime, leave = :leave, description = :description WHERE id = :id")
    suspend fun updateEffort(
        id: String,
        date: String,
        startTime: String,
        endTime: String,
        leave: Boolean,
        description: String?
    )

    @Query("DELETE FROM effort WHERE id = :id")
    suspend fun deleteEffort(id: String)
}