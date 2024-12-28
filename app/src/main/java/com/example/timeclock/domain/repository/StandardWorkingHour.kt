package com.example.timeclock.domain.repository

import com.example.timeclock.domain.model.StandardWorkingHourModel
import com.example.timeclock.domain.model.vo.StandardWorkingHourId

interface StandardWorkingHourRepository {
    fun find(id: StandardWorkingHourId): StandardWorkingHourModel?

    suspend fun save(model: StandardWorkingHourModel)
}