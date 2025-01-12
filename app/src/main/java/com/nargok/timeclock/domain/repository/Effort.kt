package com.example.timeclock.domain.repository

import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.model.EffortSearchCondition
import com.example.timeclock.domain.model.vo.EffortId
import java.time.LocalDate

/**
 * 作業記録リポジトリ
 */
interface EffortRepository {
    fun search(condition: EffortSearchCondition): List<EffortModel>

    fun find(id: EffortId): EffortModel?

    fun findByDate(date: LocalDate): EffortModel?

    suspend fun register(model: EffortModel)

    suspend fun update(model: EffortModel)
}