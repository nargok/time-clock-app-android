package com.nargok.timeclock.viewmodel.effort

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nargok.timeclock.domain.model.EffortModel
import com.nargok.timeclock.domain.model.vo.EffortDescription
import com.nargok.timeclock.domain.model.vo.EffortId
import com.nargok.timeclock.domain.repository.EffortRepository
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
    val description: String = "",
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
    private val _failedToUpdate = mutableStateOf(false)
    val failedToUpdate: State<Boolean> = _failedToUpdate
    private val _failedToDelete = mutableStateOf(false)
    val failedToDelete: State<Boolean> = _failedToDelete

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

    fun fetchEffort(id: EffortId) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = repository.find(id)
            checkNotNull(model)

            uiState = EffortEditUiState(
                id = model.id,
                selectedDate = model.date,
                startTime = model.startTime,
                endTime = model.endTime,
                description = model.description?.value ?: "",
            )
        }
    }

    fun closeFailedToUpdate() {
        _failedToUpdate.value = false
    }

    fun closeFailedToDelete() {
        _failedToDelete.value = false
    }

    fun save(id: EffortId) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = buildEffortModel(id)
            try {
                repository.update(model)
                _saveSuccess.value = true
            } catch (e: Exception) {
                _failedToUpdate.value = true
                println("EditEffortViewModel.save: $e")
            }
        }
    }

    fun delete(id: EffortId) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.delete(id)
                _saveSuccess.value = true
            } catch (e: Exception) {
                _failedToDelete.value = true
                println("EditEffortViewModel.delete: $e")
            }
        }
    }

    fun leave(id: EffortId) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = buildEffortModel(id)
            try {
                repository.update(model.leave())
                _saveSuccess.value = true
            } catch (e: Exception) {
                _failedToUpdate.value = true
                println("EditEffortViewModel.leave: $e")
            }
        }
    }

    private fun buildEffortModel(id: EffortId): EffortModel {
        return EffortModel.reconstruct(
            id = id,
            date = requireNotNull(uiState.selectedDate),
            startTime = requireNotNull(uiState.startTime),
            endTime = requireNotNull(uiState.endTime),
            leave = false,
            description = EffortDescription(uiState.description),
        )
    }
}