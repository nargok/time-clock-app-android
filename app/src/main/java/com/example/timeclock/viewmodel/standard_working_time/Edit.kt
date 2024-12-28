package com.example.timeclock.viewmodel.standard_working_time

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeclock.domain.model.StandardWorkingHourModel
import com.example.timeclock.domain.model.vo.StandardWorkingHour
import com.example.timeclock.domain.model.vo.StandardWorkingHourId
import com.example.timeclock.domain.repository.StandardWorkingHourRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class StandardWorkingTimeEditUiState(
    val id: StandardWorkingHourId? = null,
    val workingTime: StandardWorkingHour? = null,
)

@HiltViewModel
class StandardWorkingTimeEditViewModel @Inject constructor(
    private val repository: StandardWorkingHourRepository
) : ViewModel() {

    var uiState by mutableStateOf(StandardWorkingTimeEditUiState())
        private set

    private val _saveSuccess = mutableStateOf(false)
    val saveSuccess: State<Boolean> = _saveSuccess

    fun updateWorkingTime(workingTime: String) {
        uiState = uiState.copy(workingTime = StandardWorkingHour(workingTime.toInt()))
    }

    fun fetchStandardWorkingTime(yearMonth: YearMonth) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = repository.findByYearMonth(yearMonth)

            if (model != null) {
                uiState = StandardWorkingTimeEditUiState(
                    id = model.id,
                    workingTime = model.hour
                )
            }
        }
    }

    fun save(yearMonth: YearMonth) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingHour = repository.findByYearMonth(yearMonth)

            if (existingHour != null) {
                val model = StandardWorkingHourModel.reconstruct(
                    id = existingHour.id.value,
                    yearMonth = existingHour.yearMonth,
                    hour = uiState.workingTime!!.value
                )
                repository.save(model)
                return@launch
            }

            val model = StandardWorkingHourModel.create(
                yearMonth = YearMonth.now(),
                hour = requireNotNull(uiState.workingTime?.value)
            )
            repository.save(model)
        }
    }
}