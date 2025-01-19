package com.nargok.timeclock.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.nargok.timeclock.domain.model.vo.StandardWorkingHour
import com.nargok.timeclock.domain.model.vo.EffortDescription
import com.nargok.timeclock.domain.model.vo.EffortId
import java.time.Duration
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
    val description: EffortDescription? = null,
) {

    /**
     * 休暇にする
     */
    fun leave(): EffortModel {
        return EffortModel(
            id = id,
            date = date,
            startTime = LocalTime.of(9, 0),
            endTime = startTime.plusHours(WORKING_HOURS_PER_DAY.toLong() + FIXED_BREAK_TIME.toLong()),
            leave = true,
        )
    }

    /**
     * 作業時間
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun workingTime(): String {
        val duration = workingTimeDuration()
        return String.format("%.2f", duration.toHours().toDouble() + (duration.toMinutesPart() * 0.01) - FIXED_BREAK_TIME)
    }

    /**
     * 作業時間のDuration
     */
    fun workingTimeDuration(): Duration {
        return Duration.between(startTime, endTime)
    }

    companion object {
        const val FIXED_BREAK_TIME = 1.0
        const val WORKING_HOURS_PER_DAY = 8.0

        fun create(date: LocalDate, startTime: LocalTime, endTime: LocalTime, leave: Boolean, description: EffortDescription?): EffortModel {
            return EffortModel(
                id = EffortId.create(),
                date = date,
                startTime = startTime,
                endTime = endTime,
                leave = false,
                description = description,
            )
        }

        fun reconstruct(
            id: EffortId,
            date: LocalDate,
            startTime: LocalTime,
            endTime: LocalTime,
            leave: Boolean,
            description: EffortDescription?,
        ): EffortModel {
            return EffortModel(
                id = id,
                date = date,
                startTime = startTime,
                endTime = endTime,
                leave = leave,
                description = description,
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
    val standardWorkingHour: StandardWorkingHour,
    val efforts: List<EffortModel>,
) {
    /**
     * 作業時間の合計
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun totalWorkingHours(): Double {
        /**
         * 1. 作業時間の時間部分の合計 -> totalHours
         * 2. 作業時間の分部分の合計 -> totalMinutes
         * 3. 作業時間の分部分を時間に変換して整数にする => (totalMinutes / 60).toInt()
         * 4. 分合計の内60分未満を算出 => MOD(totalMinutes, 60) * 0.01
         * 5. 全部合計する
         */
        val totalHours = efforts.sumOf { it.workingTimeDuration().toHours() }
        val totalMinutes = efforts.sumOf { it.workingTimeDuration().toMinutesPart() }
        val totalMinutesToHours = (totalMinutes / 60)
        val remainingMinutes = (totalMinutes % 60) * 0.01
        return totalHours + totalMinutesToHours + remainingMinutes - efforts.size * EffortModel.FIXED_BREAK_TIME
    }

    /**
     * 作業日数
     */
    fun totalDays(): Int {
        return standardWorkingHour.value / EffortModel.WORKING_HOURS_PER_DAY.toInt()
    }

    /**
     * 経過日数
     */
    fun workedDays(): Int {
        return efforts.size
    }

    /**
     * 残りの作業日数
     */
    fun remainingDays(): Int {
        return totalDays() - efforts.size
    }

    /**
     * 作業記録リスト
     */
    fun effortList(): List<EffortModel> {
        return efforts.sortedByDescending { it.date }
    }

    /**
     * 平均作業時間
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun averageWorkingHours(): Double {
        if (efforts.isEmpty()) return 0.0
        return String.format("%.2f", totalWorkingHours() / efforts.size).toDouble()
    }
}