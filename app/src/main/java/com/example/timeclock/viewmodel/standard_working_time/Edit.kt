package com.example.timeclock.viewmodel.standard_working_time

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.timeclock.domain.model.vo.StandardWorkingHour
import com.example.timeclock.domain.model.vo.StandardWorkingHourId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class StandardWorkingTimeEditUiState(
    val id: StandardWorkingHourId? = null,
    val workingTime: StandardWorkingHour? = null,
)

@HiltViewModel
class StandardWorkingTimeEditViewModel @Inject constructor(

) : ViewModel() {

    var uiState by mutableStateOf(StandardWorkingTimeEditUiState())
        private set

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    fun updateWorkingTime(workingTime: String) {
        uiState = uiState.copy(workingTime = StandardWorkingHour(workingTime.toInt()))
    }

    fun fetchStandardWorkingTime(id: StandardWorkingHourId) {}

    fun save() {}
}