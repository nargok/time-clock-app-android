package com.example.timeclock.domain.repository

import com.example.timeclock.domain.model.StandardWorkingTimeModel
import java.time.YearMonth

interface StandardWorkingTimeRepository {
    fun find(yearMonth: YearMonth): StandardWorkingTimeModel?

    fun save(model: StandardWorkingTimeModel)
}