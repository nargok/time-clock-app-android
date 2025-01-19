package com.nargok.timeclock.ui.view.effort

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nargok.timeclock.Greeting
import com.nargok.timeclock.ui.theme.TimeClockTheme
import com.nargok.timeclock.ui.view.effort.components.EffortTimeDialog
import com.nargok.timeclock.viewmodel.effort.EffortRegisterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String,
    modifier: Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortRegisterScreen(
    navController: NavController,
    viewModel: EffortRegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    val uiState = viewModel.uiState
    val snackBarHostState = remember { SnackbarHostState() }
    val saveSuccess by viewModel.saveSuccess
    val failedToRegister by viewModel.failedToRegister

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            onSaveSuccess()
        }
    }

    LaunchedEffect(failedToRegister) {
        if (failedToRegister) {
            snackBarHostState.showSnackbar(
                message = "保存に失敗しました",
                duration = SnackbarDuration.Short
            )
            viewModel.clearFailedToRegister()
        }
    }

    Scaffold(
        topBar = {
            Header(
                title = "勤務時間登録",
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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

                Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(16.dp))

                // 備考
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { viewModel.updateDescription(it) },
                    label = { Text("備考") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.save() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("保存")
                }

                // 残りのスペースを確保
                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = { viewModel.leave() },
                    modifier = Modifier
                        .padding(32.dp)
                ) {
                    Text("休暇にする")
                }
            }
            SnackbarHost(hostState = snackBarHostState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EffortRegisterPreview() {
    TimeClockTheme {
        EffortRegisterScreen(
            navController = rememberNavController(),
            onNavigateBack = {},
            onSaveSuccess = {}
        )
    }
}
