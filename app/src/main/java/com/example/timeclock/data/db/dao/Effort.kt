package com.example.timeclock.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.timeclock.data.db.entity.EffortEntity

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

    @Query("UPDATE effort SET start_time = :startTime, end_time = :endTime, leave = :leave WHERE id = :id")
    suspend fun updateEffort(id: String, startTime: String, endTime: String, leave: Boolean)
}