package com.example.timeclock.viewmodel.effort

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.model.vo.EffortDescription
import com.example.timeclock.domain.repository.EffortRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

data class EffortUiState(
    val selectedDate: LocalDate = LocalDate.now(), // TODO まだ未登録の日付を初期値とする
    val startTime: LocalTime = LocalTime.parse("09:00"), // TODO customize default start time
    val endTime: LocalTime = LocalTime.parse("18:00"), // TODO customize default end time
    val description: String = "",
    val showDatePicker: Boolean = false,
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false,
)

@HiltViewModel
class EffortRegisterViewModel @Inject constructor(
    private val repository: EffortRepository
) : ViewModel() {

    var uiState by mutableStateOf(EffortUiState())
        private set

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

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

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO handle create or update
            val model = EffortModel.create(
                date = uiState.selectedDate,
                startTime = uiState.startTime,
                endTime = uiState.endTime,
                leave = false, // TODO add uiState
                description = EffortDescription(uiState.description),
            )
            // TODO ここで登録ができなくなった。
            // TODO handle register or update
            repository.save(model)
            // TODO handle success or failure
            _saveSuccess.value = true
        }
    }
}