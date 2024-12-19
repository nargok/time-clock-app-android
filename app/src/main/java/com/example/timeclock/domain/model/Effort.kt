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

    /**
     * 作業時間
     */
    fun workingTime(): Double {
        // TODO 作業時間の計算ロジックを修正する
        return (endTime.hour - startTime.hour).toDouble()
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

/**
 * 月次作業記録
 */
data class MonthlyEffortModel(
    val yearMonth: YearMonth,
    val efforts: List<EffortModel>,
) {
    /**
     * 作業時間の合計
     */
    fun totalWorkingHours(): Double {
        return efforts.sumOf { it.workingTime() }
    }

    /**
     * 残りの作業日数
     */
    fun remainingDays(): Int {
        val totalDays = TOTAL_WORKING_HOURS / WORKING_HOURS_PER_DAY
        return totalDays - efforts.size
    }

    /**
     * 平均作業時間
     */
    fun averageWorkingHours(): Double {
        if (efforts.isEmpty()) return 0.0
        return String.format("%.1f", totalWorkingHours().toDouble() / efforts.size).toDouble()
    }

    companion object {
        // TODO get it from database
        private val TOTAL_WORKING_HOURS = 160
        private val WORKING_HOURS_PER_DAY = 8
    }
}