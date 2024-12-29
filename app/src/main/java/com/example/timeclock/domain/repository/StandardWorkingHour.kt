package com.example.timeclock.domain.repository

import com.example.timeclock.domain.model.StandardWorkingHourModel
import com.example.timeclock.domain.model.vo.StandardWorkingHourId
import java.time.YearMonth

/**
 * 標準労働時間リポジトリ
 */
interface StandardWorkingHourRepository {
    fun find(id: StandardWorkingHourId): StandardWorkingHourModel?

    fun findByYearMonth(yearMonth: YearMonth): StandardWorkingHourModel?

    suspend fun save(model: StandardWorkingHourModel)
}