package com.example.timeclock.viewmodel.effort

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.model.vo.EffortId
import com.example.timeclock.domain.repository.EffortRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

data class EffortEditUiState(
    val id: EffortId? = null,
    val selectedDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val showDatePicker: Boolean = false,
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false,
)

@HiltViewModel
class EffortEditViewModel @Inject constructor(
    private val repository: EffortRepository
) : ViewModel() {

    var uiState by mutableStateOf(EffortEditUiState())
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

    fun toggleDatePicker(show: Boolean) {
        uiState = uiState.copy(showDatePicker = show)
    }

    fun toggleStartTimePicker(show: Boolean) {
        uiState = uiState.copy(showStartTimePicker = show)
    }

    fun toggleEndTimePicker(show: Boolean) {
        uiState = uiState.copy(showEndTimePicker = show)
    }

    fun fetchEffort(id: EffortId) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = repository.find(id)
            checkNotNull(model)

            uiState = EffortEditUiState(
                id = model.id,
                selectedDate = model.date,
                startTime = model.startTime,
                endTime = model.endTime,
            )
        }
    }

    fun save(id: EffortId) {
        viewModelScope.launch(Dispatchers.IO) {
            val exitingEffort = repository.find(id)
            checkNotNull(exitingEffort)

            val model = EffortModel.reconstruct(
                id = id,
                date = exitingEffort.date,
                startTime = requireNotNull(uiState.startTime),
                endTime = requireNotNull(uiState.endTime),
                leave = false, // TODO set it from uiState
            )
            // TODO handle register or update
            repository.save(model)
            // TODO handle success or failure
            _saveSuccess.value = true
        }
    }
}