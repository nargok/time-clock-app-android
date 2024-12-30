package com.example.timeclock.domain.model.vo

import de.huxhorn.sulky.ulid.ULID

/**
 * 標準勤務時間ID
 */
data class StandardWorkingHourId private constructor(val value: String){
    companion object {
        private val ulid = ULID()

        fun create(): StandardWorkingHourId {
            return StandardWorkingHourId(ulid.nextULID())
        }

        fun reconstruct(id: String): StandardWorkingHourId {
            return StandardWorkingHourId(id)
        }
    }

    override fun toString(): String {
        return value
    }
}

/**
 * 標準勤務時間
 */
data class StandardWorkingHour(val value: Int) {

    init {
        require(value in 0..MAX_TIME) {
            "Standard working time must be between 0 and $MAX_TIME."
        }
    }

    companion object {
        const val MAX_TIME = 744 // 24時間 * 31日
    }
}