package com.example.timeclock.infrastructure.repository

import com.example.timeclock.domain.model.StandardWorkingTimeModel
import com.example.timeclock.domain.repository.StandardWorkingTimeRepository
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandardWorkingTimeRepositoryImpl @Inject constructor(

): StandardWorkingTimeRepository {
    override fun find(yearMonth: YearMonth): StandardWorkingTimeModel? {
        TODO("Not yet implemented")
    }

    override fun save(model: StandardWorkingTimeModel) {
        TODO("Not yet implemented")
    }
}