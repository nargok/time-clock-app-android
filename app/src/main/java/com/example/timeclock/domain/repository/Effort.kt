package com.example.timeclock.domain.repository

import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.model.EffortSearchCondition

/**
 * 作業記録リポジトリ
 */
interface EffortRepository {
    fun search(condition: EffortSearchCondition): List<EffortModel>

    suspend fun save(model: EffortModel)
}