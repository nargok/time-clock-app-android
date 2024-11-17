package com.example.timeclock.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.timeclock.data.db.dao.EffortDao
import com.example.timeclock.data.db.entity.EffortEntity

@Database(entities = [EffortEntity::class], version = 1)
abstract class TimeClockDatabase : RoomDatabase() {
    abstract fun effortDao(): EffortDao

    companion object {
        const val DATABASE_NAME = "time_clock_database"
    }
}