package com.example.timeclock.domain.repository

import com.example.timeclock.domain.model.StandardWorkingHourModel
import java.time.YearMonth

interface StandardWorkingHourRepository {
    fun find(yearMonth: YearMonth): StandardWorkingHourModel?

    fun save(model: StandardWorkingHourModel)
}