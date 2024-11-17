package com.example.timeclock.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.timeclock.data.db.entity.EffortEntity

@Dao
interface EffortDao {
    @Query("SELECT * FROM effort WHERE date >= :fromDate AND date <= :toDate ORDER BY id DESC")
    fun getEfforts(fromDate: String, toDate: String): List<EffortEntity>

    @Insert
    suspend fun insertEffort(post: EffortEntity)
}