package com.example.timeclock.domain.repository

import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.model.EffortSearchCondition
import com.example.timeclock.domain.model.vo.EffortId

/**
 * 作業記録リポジトリ
 */
interface EffortRepository {
    fun search(condition: EffortSearchCondition): List<EffortModel>

    fun find(id: EffortId): EffortModel?

    suspend fun save(model: EffortModel)
}