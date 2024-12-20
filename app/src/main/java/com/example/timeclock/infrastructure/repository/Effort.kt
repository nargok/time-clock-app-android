package com.example.timeclock.infrastructure.repository

import com.example.timeclock.data.db.dao.EffortDao
import com.example.timeclock.data.db.entity.EffortEntity
import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.model.EffortSearchCondition
import com.example.timeclock.domain.model.vo.EffortDescription
import com.example.timeclock.domain.model.vo.EffortId
import com.example.timeclock.domain.repository.EffortRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EffortRepositoryImpl @Inject constructor(
    private val effortDao: EffortDao,
): EffortRepository {
    override fun search(condition: EffortSearchCondition): List<EffortModel> {
        val yearMonth = condition.yearMonth
        val start = yearMonth.atDay(1).toString()
        val end = yearMonth.atEndOfMonth().toString()

        return effortDao.getEfforts(start, end).map { it.toModel() }
    }

    override fun find(id: EffortId) = effortDao.findEffortById(id.value)?.let { it.toModel() }

    override suspend fun save(model: EffortModel) {
        val existingEffort = effortDao.findEffortByDate(model.date.toString())

        if (existingEffort == null) {
            effortDao.insertEffort(model.toEntity())
            return
        }

        effortDao.updateEffort(
            id = model.id.value,
            startTime = model.startTime.toString(),
            endTime = model.endTime.toString(),
            leave = model.leave,
            description = model.description?.value,
        )
    }
}

/**
 * Convert [EffortModel] to [EffortEntity]
 */
private fun EffortModel.toEntity(): EffortEntity {
    return EffortEntity(
        id = id.value,
        date = date.toString(),
        startTime = startTime.toString(),
        endTime = endTime.toString(),
        leave = leave,
        description = description?.let { it.value },
    )
}

/**
 * Convert [EffortEntity] to [EffortModel]
 */
private fun EffortEntity.toModel(): EffortModel {
    return EffortModel.reconstruct(
        id = EffortId.reconstruct(id),
        date = LocalDate.parse(date),
        startTime = LocalTime.parse(startTime),
        endTime = LocalTime.parse(endTime),
        leave = leave,
        description = description?.let { EffortDescription(it) },
    )
}
