package com.example.timeclock.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timeclock.viewmodel.EffortViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortRegisterScreen(viewModel: EffortViewModel = viewModel()) {
    val datePickerState = rememberDatePickerState()
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 日付
        OutlinedTextField(
            value = uiState.selectedDate?.format(DateTimeFormatter.ISO_DATE) ?: "",
            onValueChange = {},
            label = { Text("日付") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.toggleDatePicker(true)
                }) {
                    Icon(Icons.Filled.CalendarToday, contentDescription = "日付選択")
                }
            }
        )

        if (uiState.showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { viewModel.toggleDatePicker(false) },
                confirmButton = {
                    Button(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.updateDate(LocalDate.ofEpochDay(it / 86400000))
                        }
                        viewModel.toggleDatePicker(false)
                    }) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.toggleDatePicker(false) }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // 開始時間
        OutlinedTextField(
            value = uiState.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            onValueChange = {},
            label = { Text("開始") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.toggleStartTimePicker(true)
                }) {
                    Icon(Icons.Filled.Schedule, contentDescription = "開始時間選択")
                }
            }
        )

        if (uiState.showStartTimePicker) {
            EffortTimeDialog(
                state = rememberTimePickerState(
                    initialHour = uiState.startTime.hour,
                    initialMinute = uiState.startTime.minute,
                    is24Hour = true,
                ),
                onConfirm = {
                    viewModel.updateStartTime(LocalTime.of(it.hour, it.minute))
                    viewModel.toggleStartTimePicker(false)
                },
                onDismiss = { viewModel.toggleStartTimePicker(false) }
            )
        }

        // 終了時間
        OutlinedTextField(
            value = uiState.endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            onValueChange = {},
            label = { Text("終了") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.toggleEndTimePicker(true)
                }) {
                    Icon(Icons.Filled.Schedule, contentDescription = "終了時間選択")
                }
            }
        )

        if (uiState.showEndTimePicker) {
            EffortTimeDialog(
                state = rememberTimePickerState(
                    initialHour = uiState.endTime.hour,
                    initialMinute = uiState.endTime.minute,
                    is24Hour = true,
                ),
                onConfirm = {
                    viewModel.updateEndTime(LocalTime.of(it.hour, it.minute))
                    viewModel.toggleEndTimePicker(false)
                },
                onDismiss = { viewModel.toggleEndTimePicker(false) }
            )
        }

        Button(onClick = {
            println(
                """
                日付: ${uiState.selectedDate}
                開始: ${uiState.startTime}
                終了: ${uiState.endTime}
            """.trimIndent()
            )
        }) {
            Text("保存")
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        text = { content() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortTimeDialog(
    state: TimePickerState,
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(state) },
    ) {
        TimePicker(
            state = state,
        )
    }
}