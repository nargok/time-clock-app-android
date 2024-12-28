package com.example.timeclock.infrastructure.repository

import com.example.timeclock.data.db.dao.StandardWorkingHourDao
import com.example.timeclock.data.db.entity.StandardWorkingHourEntity
import com.example.timeclock.domain.model.StandardWorkingHourModel
import com.example.timeclock.domain.model.vo.StandardWorkingHourId
import com.example.timeclock.domain.repository.StandardWorkingHourRepository
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandardWorkingHourRepositoryImpl @Inject constructor(
    private val standardWorkingHourDao: StandardWorkingHourDao,
) : StandardWorkingHourRepository {

    override fun find(id: StandardWorkingHourId) =
        standardWorkingHourDao.findByYearMonth(id.toString())?.let { it.toModel() }

    override suspend fun save(model: StandardWorkingHourModel) {
        val existingStandardWorkingHour = standardWorkingHourDao.findByYearMonth(model.yearMonth.toString())

        if (existingStandardWorkingHour == null) {
            standardWorkingHourDao.insertStandardWorkingHour(model.toEntity())
            return
        }

        standardWorkingHourDao.updateStandardWorkingHour(
            id = model.id.value,
            hour = model.hour.value,
        )
    }
}

/**
 * Convert [StandardWorkingHourModel] to [StandardWorkingHourEntity]
 */
private fun StandardWorkingHourModel.toEntity(): StandardWorkingHourEntity {
    return StandardWorkingHourEntity(
        id = id.value,
        yearMonth = yearMonth.toString(),
        hour = hour.value,
    )
}

/**
 * Convert [StandardWorkingHourEntity] to [StandardWorkingHourModel]
 */
private fun StandardWorkingHourEntity.toModel(): StandardWorkingHourModel {
    return StandardWorkingHourModel.reconstruct(
        id = id,
        yearMonth = YearMonth.parse(yearMonth),
        hour = hour,
    )
}