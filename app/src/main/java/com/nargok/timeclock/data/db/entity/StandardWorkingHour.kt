package com.nargok.timeclock.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "standard_working_hour",
    indices = [
        Index(value = ["year_month"], unique = true)
    ]
)
data class StandardWorkingHourEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name="year_month") val yearMonth: String,
    @ColumnInfo(name = "hour") val hour: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)