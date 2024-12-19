package com.example.timeclock.viewmodel.effort

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.domain.model.EffortSearchCondition
import com.example.timeclock.domain.model.MonthlyEffortModel
import com.example.timeclock.domain.repository.EffortRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class EffortListUiState(
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val displayEffortSummary: Boolean = false,
)

@HiltViewModel
class EffortListViewModel @Inject constructor(
    private val repository: EffortRepository
) : ViewModel() {

    var uiState by mutableStateOf(EffortListUiState())
        private set

    private val _efforts = mutableStateOf<List<EffortModel>>(emptyList())
    val efforts: State<List<EffortModel>> = _efforts
    private val _monthlyEfforts = mutableStateOf<MonthlyEffortModel?>(null)
    val monthlyEfforts: State<MonthlyEffortModel?> = _monthlyEfforts

    fun setPreviousYearMonth() {
        uiState = uiState.copy(selectedYearMonth = uiState.selectedYearMonth.minusMonths(1))
        fetchMonthlyEfforts()
    }

    fun setNextYearMonth() {
        uiState = uiState.copy(selectedYearMonth = uiState.selectedYearMonth.plusMonths(1))
        fetchMonthlyEfforts()
    }

    fun toggleDisplayEffortSummary(show: Boolean) {
        uiState = uiState.copy(displayEffortSummary = show)
    }

    fun fetchMonthlyEfforts() {
        viewModelScope.launch(Dispatchers.IO) {
            val condition =
                EffortSearchCondition(uiState.selectedYearMonth)
            val efforts = repository.search(condition)
            _efforts.value = efforts
            _monthlyEfforts.value = MonthlyEffortModel(uiState.selectedYearMonth, efforts)
        }
    }
}
