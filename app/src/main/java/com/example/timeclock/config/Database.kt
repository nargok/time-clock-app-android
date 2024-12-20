package com.example.timeclock.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.timeclock.data.db.dao.EffortDao
import com.example.timeclock.data.db.entity.EffortEntity

@Database(entities = [EffortEntity::class], version = 2)
abstract class TimeClockDatabase : RoomDatabase() {
    abstract fun effortDao(): EffortDao

    companion object {
        const val DATABASE_NAME = "time_clock_database"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE effort ADD COLUMN description TEXT")
                database.execSQL("CREATE INDEX index_effort_date ON effort(date)")
            }
        }
    }
}