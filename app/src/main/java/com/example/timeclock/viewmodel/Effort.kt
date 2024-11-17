package com.example.timeclock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.repository.EffortRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class EffortViewModel @Inject constructor(
    private val repository: EffortRepository
) : ViewModel() {

    fun register(date: LocalDate, startTime: LocalTime, endTime: LocalTime) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = EffortModel.create(date, startTime, endTime)
            repository.register(model)
        }
    }
}