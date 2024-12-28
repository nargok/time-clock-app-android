package com.example.timeclock.infrastructure.repository

import com.example.timeclock.data.db.dao.StandardWorkingHourDao
import com.example.timeclock.domain.model.StandardWorkingHourModel
import com.example.timeclock.domain.repository.StandardWorkingHourRepository
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandardWorkingHourRepositoryImpl @Inject constructor(
    private val standardWorkingHourDao: StandardWorkingHourDao,
): StandardWorkingHourRepository {

    override fun find(yearMonth: YearMonth): StandardWorkingHourModel? {
        TODO("Not yet implemented")
    }

    override fun save(model: StandardWorkingHourModel) {
        TODO("Not yet implemented")
    }
}
