package com.example.timeclock.domain.model

import com.example.timeclock.domain.model.vo.StandardWorkingHour
import com.example.timeclock.domain.model.vo.StandardWorkingHourId
import java.time.YearMonth

data class StandardWorkingHourModel private constructor(
    val id: StandardWorkingHourId,
    val yearMonth: YearMonth,
    val hour: StandardWorkingHour,
) {
    companion object {
        fun create(yearMonth: YearMonth, hour: Int): StandardWorkingHourModel {
            return StandardWorkingHourModel(
                id = StandardWorkingHourId.create(),
                yearMonth = yearMonth,
                hour = StandardWorkingHour(hour)
            )
        }

        fun reconstruct(id: String, yearMonth: YearMonth, hour: Int): StandardWorkingHourModel {
            return StandardWorkingHourModel(
                id = StandardWorkingHourId.reconstruct(id),
                yearMonth = yearMonth,
                hour = StandardWorkingHour(hour)
            )
        }
    }
}
