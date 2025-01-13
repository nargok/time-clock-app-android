package com.nargok.timeclock.viewmodel.effort

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nargok.timeclock.domain.model.EffortModel
import com.nargok.timeclock.domain.model.vo.EffortDescription
import com.nargok.timeclock.domain.service.EffortService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

data class EffortUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.parse("09:00"), // TODO customize default start time
    val endTime: LocalTime = LocalTime.parse("18:00"), // TODO customize default end time
    val description: String = "",
    val showDatePicker: Boolean = false,
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false,
)

@HiltViewModel
class EffortRegisterViewModel @Inject constructor(
    private val effortService: EffortService
) : ViewModel() {

    var uiState by mutableStateOf(EffortUiState())
        private set

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess
    private val _failedToRegister = mutableStateOf(false)
    val failedToRegister: State<Boolean> = _failedToRegister

    fun updateDate(date: LocalDate) {
        uiState = uiState.copy(selectedDate = date)
    }

    fun updateStartTime(time: LocalTime) {
        uiState = uiState.copy(startTime = time)
    }

    fun updateEndTime(time: LocalTime) {
        uiState = uiState.copy(endTime = time)
    }

    fun updateDescription(description: String) {
        uiState = uiState.copy(description = description)
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

    fun clearFailedToRegister() {
        _failedToRegister.value = false
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            val model = EffortModel.create(
                date = uiState.selectedDate,
                startTime = uiState.startTime,
                endTime = uiState.endTime,
                leave = false, // TODO add uiState
                description = EffortDescription(uiState.description),
            )
            try {
                effortService.register(model)
                _saveSuccess.value = true
            } catch (e: Exception) {
                _failedToRegister.value = true
            }
        }
    }
}