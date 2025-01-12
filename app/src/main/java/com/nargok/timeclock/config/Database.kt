package com.example.timeclock.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.timeclock.data.db.dao.EffortDao
import com.example.timeclock.data.db.dao.StandardWorkingHourDao
import com.example.timeclock.data.db.entity.EffortEntity
import com.example.timeclock.data.db.entity.StandardWorkingHourEntity

@Database(
    entities = [
        EffortEntity::class,
        StandardWorkingHourEntity::class,
    ],
    version = 5
)
abstract class TimeClockDatabase : RoomDatabase() {
    abstract fun effortDao(): EffortDao
    abstract fun standardWorkingHourDao(): StandardWorkingHourDao

    companion object {
        const val DATABASE_NAME = "time_clock_database"

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP INDEX IF EXISTS `index_effort_date`;")
                database.execSQL("CREATE UNIQUE INDEX index_effort_date ON effort(date)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE standard_working_hour (id TEXT NOT NULL, year_month TEXT NOT NULL, hour INTEGER NOT NULL, created_at INTEGER NOT NULL, PRIMARY KEY(id))")
                database.execSQL("CREATE UNIQUE INDEX index_standard_working_hour_year_month ON standard_working_hour(year_month)")
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE effort ADD COLUMN description TEXT")
                database.execSQL("CREATE INDEX index_effort_date ON effort(date)")
            }
        }
    }
}