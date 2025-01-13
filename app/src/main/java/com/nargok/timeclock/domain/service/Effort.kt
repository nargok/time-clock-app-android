package com.nargok.timeclock.domain.service

import com.nargok.timeclock.domain.model.EffortModel
import java.time.LocalDate

/**
 * 作業記録ドメインサービス
 */
interface EffortService {
    /**
     * 作業記録を登録する
     */
    suspend fun register(model: EffortModel)

    /**
     * 作業日が登録済かどうかを確認する
     */
    fun checkExistEffortDate(date: LocalDate): Boolean
}