package com.example.timeclock.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

data class EffortUiState(
    val selectedDate: LocalDate? = LocalDate.now(),
    val startTime: LocalTime = LocalTime.parse("09:00"), // TODO customize default start time
    val endTime: LocalTime = LocalTime.parse("18:00"), // TODO customize default end time
    val showDatePicker: Boolean = false,
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false,
)

@HiltViewModel
class EffortViewModel @Inject constructor(
    private val repository: EffortRepository
) : ViewModel() {

    var uiState by mutableStateOf(EffortUiState())
        private set

    fun updateDate(date: LocalDate) {
        uiState = uiState.copy(selectedDate = date)
    }

    fun updateStartTime(time: LocalTime) {
        uiState = uiState.copy(startTime = time)
    }

    fun updateEndTime(time: LocalTime) {
        uiState = uiState.copy(endTime = time)
    }

    fun toggleDatePicker(show: Boolean) {
        uiState = uiState.copy(showDatePicker = show)
    }

    fun toggleStartTimePicker(show: Boolean) {
        uiState = uiState.copy(showStartTimePicker = show)
    }

    fun toggleEndTimePicker(show: Boolean) {
        uiState = uiState.copy(showEndTimePicker = show)
    }

    fun register(date: LocalDate, startTime: LocalTime, endTime: LocalTime) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = EffortModel.create(date, startTime, endTime)
            repository.register(model)
        }
    }
}