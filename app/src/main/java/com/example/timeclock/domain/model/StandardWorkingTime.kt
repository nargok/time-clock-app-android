package com.example.timeclock.domain.model

import com.example.timeclock.domain.model.vo.StandardWorkingTime
import com.example.timeclock.domain.model.vo.StandardWorkingTimeId
import java.time.YearMonth

data class StandardWorkingTimeModel private constructor(
    val id: StandardWorkingTimeId,
    val yearMonth: YearMonth,
    val hour: StandardWorkingTime,
) {
    companion object {
        fun create(yearMonth: YearMonth, hour: Int): StandardWorkingTimeModel {
            return StandardWorkingTimeModel(
                id = StandardWorkingTimeId.create(),
                yearMonth = yearMonth,
                hour = StandardWorkingTime(hour)
            )
        }

        fun reconstruct(id: String, yearMonth: YearMonth, hour: Int): StandardWorkingTimeModel {
            return StandardWorkingTimeModel(
                id = StandardWorkingTimeId.reconstruct(id),
                yearMonth = yearMonth,
                hour = StandardWorkingTime(hour)
            )
        }
    }
}
