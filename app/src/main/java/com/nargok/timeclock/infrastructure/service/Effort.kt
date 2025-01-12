package com.nargok.timeclock.infrastructure.service

import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.repository.EffortRepository
import com.nargok.timeclock.domain.service.EffortService
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EffortServiceImpl @Inject constructor(
    private val effortRepository: EffortRepository
) : EffortService {

    /**
     * 作業記録を登録する
     */
    override suspend fun register(model: EffortModel) {
        if (checkExistEffortDate(model.date)) {
            throw IllegalArgumentException("Effort date already exists")
        }

        effortRepository.register(model)
    }

    /**
     * 作業日が登録済かどうかを確認する
     */
    override fun checkExistEffortDate(date: LocalDate): Boolean {
        return effortRepository.findByDate(date) != null
    }
}