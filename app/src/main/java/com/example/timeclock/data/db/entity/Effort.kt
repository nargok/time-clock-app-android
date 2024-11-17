package com.example.timeclock.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName="effort")
data class EffortEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "start_time")val startTime: String,
    @ColumnInfo(name = "end_time")val endTime: String,
    @ColumnInfo(name = "leave")val leave: Boolean,
    @ColumnInfo(name = "created_at")val createdAt: Long = System.currentTimeMillis()
)