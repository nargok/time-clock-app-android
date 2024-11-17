package com.example.timeclock.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortRegisterScreen() {
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()
    val formattedDate = remember(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            val selectedDate = LocalDate.ofEpochDay(datePickerState.selectedDateMillis!! / 86400000)
            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } ?: ""
    }

    var showStartTimePicker by remember {
        mutableStateOf(false)
    }
    val startTimePickerState = rememberTimePickerState()
    val formattedStartTime = remember(startTimePickerState.hour, startTimePickerState.minute) {
        LocalTime.of(startTimePickerState.hour, startTimePickerState.minute)
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    var showEndTimePicker by remember {
        mutableStateOf(false)
    }
    val endTimePickerState = rememberTimePickerState()
    val formattedEndTime = remember(endTimePickerState.hour, endTimePickerState.minute) {
        LocalTime.of(endTimePickerState.hour, endTimePickerState.minute)
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }


    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 日付
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            label = { Text("日付") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    showDatePicker = true
                }) {
                    Icon(Icons.Filled.CalendarToday, contentDescription = "日付選択")
                }
            }
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(onClick = { showDatePicker = false }) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // 開始時間
        OutlinedTextField(
            value = formattedStartTime,
            onValueChange = {},
            label = { Text("開始") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    showStartTimePicker = true
                }) {
                    Icon(Icons.Filled.Schedule, contentDescription = "開始時間選択")
                }
            }
        )


        OutlinedTextField(
            value = formattedEndTime,
            onValueChange = {},
            label = { Text("終了") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    showEndTimePicker = true
                }) {
                    Icon(Icons.Filled.Schedule, contentDescription = "終了時間選択")
                }
            }
        )


        Button(onClick = {
            println(
                """
                日付: $date
                開始: $startTime
                終了: $endTime
            """.trimIndent()
            )
        }) {
            Text("保存")
        }
    }


}