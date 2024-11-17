package com.example.timeclock.domain.model

import com.example.timeclock.config.defaultWorkingHours
import com.example.timeclock.domain.model.vo.EffortId
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

/**
 * 作業記録
 */
data class EffortModel private constructor(
    val id: EffortId,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val leave: Boolean,
) {

    /**
     * 休暇にする
     */
    fun makeLeave(): EffortModel {
        return EffortModel(
            id = id,
            date = date,
            startTime = LocalTime.of(9, 0),
            endTime = startTime.plusHours(defaultWorkingHours),
            leave = true,
        )
    }

    companion object {
        fun create(date: LocalDate, startTime: LocalTime, endTime: LocalTime): EffortModel {
            return EffortModel(
                id = EffortId.create(),
                date = date,
                startTime = startTime,
                endTime = endTime,
                leave = false,
            )
        }

        fun reconstruct(
            id: EffortId,
            date: LocalDate,
            startTime: LocalTime,
            endTime: LocalTime,
            leave: Boolean
        ): EffortModel {
            return EffortModel(
                id = id,
                date = date,
                startTime = startTime,
                endTime = endTime,
                leave = leave,
            )
        }
    }
}

/**
 * 作業記録検索条件
 */
data class EffortSearchCondition(
    val yearMonth: YearMonth,
)