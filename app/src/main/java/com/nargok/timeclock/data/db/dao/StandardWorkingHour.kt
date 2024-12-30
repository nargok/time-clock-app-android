package com.example.timeclock.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.timeclock.data.db.entity.StandardWorkingHourEntity

@Dao
interface StandardWorkingHourDao {
    @Query("SELECT * FROM standard_working_hour WHERE id = :id")
    fun findById(id: String): StandardWorkingHourEntity?

    @Query("SELECT * FROM standard_working_hour WHERE year_month = :yearMonth")
    fun findByYearMonth(yearMonth: String): StandardWorkingHourEntity?

    @Insert
    suspend fun insertStandardWorkingHour(standardWorkingHour: StandardWorkingHourEntity)

    @Query("UPDATE standard_working_hour SET hour = :hour WHERE id = :id")
    suspend fun updateStandardWorkingHour(id: String, hour: Int)
}
