package com.example.timeclock.domain.model.vo
import de.huxhorn.sulky.ulid.ULID

/**
 * 作業記録ID
 */
data class EffortId private constructor(val value: String) {
    companion object {
        private val ulid = ULID()
        fun create(): EffortId {
            return EffortId(ulid.nextULID())
        }

        fun reconstruct(id: String): EffortId {
            return EffortId(id)
        }
    }

    override fun toString(): String {
        return value
    }
}
