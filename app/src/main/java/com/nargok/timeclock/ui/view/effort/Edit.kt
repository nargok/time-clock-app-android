package com.nargok.timeclock.ui.view.effort

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.Surface
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nargok.timeclock.domain.model.vo.EffortId
import com.nargok.timeclock.ui.view.effort.components.EffortTimeDialog
import com.nargok.timeclock.viewmodel.effort.EffortEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderWithDeleteButton(
    title: String,
    onDelete: () -> Unit,
    modifier: Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )

        },
        actions = {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "削除",
                )
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortEditScreen(
    id: EffortId,
    navController: NavController,
    viewModel: EffortEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    val uiState = viewModel.uiState
    val snackBarHostState = remember { SnackbarHostState() }
    val saveSuccess by viewModel.saveSuccess
    val failedToUpdate by viewModel.failedToUpdate
    val failedToDelete by viewModel.failedToDelete

    LaunchedEffect(Unit) {
        viewModel.fetchEffort(id)
    }

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            onSaveSuccess()
        }
    }

    LaunchedEffect(failedToUpdate) {
        if (failedToUpdate) {
            snackBarHostState.showSnackbar(
                message = "更新に失敗しました",
                duration = SnackbarDuration.Short
            )
            viewModel.closeFailedToUpdate()
        }
    }

    LaunchedEffect(failedToDelete) {
        if (failedToDelete) {
            snackBarHostState.showSnackbar(
                message = "削除に失敗しました",
                duration = SnackbarDuration.Short
            )
            viewModel.closeFailedToDelete()
        }
    }

    Scaffold(
        topBar = {
            HeaderWithDeleteButton(
                title = "勤務時間編集",
                onDelete = { viewModel.delete(id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
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
                verticalArrangement = Arrangement.Center,
            ) {
                // 日付
                Surface(onClick = {
                    viewModel.toggleDatePicker(true)
                }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "日付選択")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.selectedDate?.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
                                ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }


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
                Surface(onClick = {
                    viewModel.toggleStartTimePicker(true)
                }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("開始")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.startTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }


                if (uiState.showStartTimePicker) {
                    EffortTimeDialog(
                        state = rememberTimePickerState(
                            initialHour = uiState.startTime?.hour ?: 9,
                            initialMinute = uiState.startTime?.minute ?: 0,
                            is24Hour = true,
                        ),
                        onConfirm = {
                            viewModel.updateStartTime(LocalTime.of(it.hour, it.minute))
                            viewModel.toggleStartTimePicker(false)
                        },
                        onDismiss = { viewModel.toggleStartTimePicker(false) }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 終了時間
                Surface(onClick = {
                    viewModel.toggleEndTimePicker(true)
                }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("終了")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.endTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (uiState.showEndTimePicker) {
                    EffortTimeDialog(
                        state = rememberTimePickerState(
                            initialHour = uiState.endTime?.hour ?: 18,
                            initialMinute = uiState.endTime?.minute ?: 0,
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
                    onClick = { viewModel.save(id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("保存")
                }

                // 残りのスペースを確保
                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = { viewModel.leave(id) },
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
