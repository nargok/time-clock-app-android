package com.example.timeclock.ui.view.effort

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.viewmodel.effort.EffortListViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortListScreen(
    navController: NavController,
    viewModel: EffortListViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val efforts by viewModel.efforts
    val monthlyEffort by viewModel.monthlyEfforts

    LaunchedEffect(Unit) {
        viewModel.fetchMonthlyEfforts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        IconButton(onClick = { viewModel.setPreviousYearMonth() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
                        }
                        Text("作業記録 ${uiState.selectedYearMonth.year}/${uiState.selectedYearMonth.monthValue}")
                        IconButton(onClick = { viewModel.setNextYearMonth() }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Next  Month")
                        }
                        IconButton(onClick = {
                            navController.navigate("standardWorkingHourEdit/${uiState.selectedYearMonth}")
                        }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Go to Standard working hours"
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("effortRegister") }) {
                Icon(Icons.Filled.Add, contentDescription = "")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                // FIXME 最初から全項目表示してもよいかも
                if (uiState.displayEffortSummary) {
                    Text(
                        "基準時間: 160時間(${monthlyEffort?.totalDays()}日)", // TODO 設定した基準時間を表示する
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { viewModel.toggleDisplayEffortSummary(!uiState.displayEffortSummary) },
                        fontSize = 24.sp
                    )
                    Text(
                        "合計時間: ${monthlyEffort?.totalWorkingHours()}時間(${monthlyEffort?.workedDays()}日)",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { viewModel.toggleDisplayEffortSummary(!uiState.displayEffortSummary) },
                        fontSize = 24.sp
                    )
                    Text(
                        "残り日数: ${monthlyEffort?.remainingDays()}日",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { viewModel.toggleDisplayEffortSummary(!uiState.displayEffortSummary) },
                        fontSize = 24.sp
                    )
                }
                Text(
                    "平均: ${monthlyEffort?.averageWorkingHours()}H",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable { viewModel.toggleDisplayEffortSummary(!uiState.displayEffortSummary) },
                    fontSize = 24.sp
                )
            }
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                items(efforts) { effort ->
                    EffortListItem(effort) {
                        navController.navigate("effortEdit/${effort.id.value}")
                    }
                    Divider()
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun EffortListItem(effort: EffortModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column {
            Text(
                text = "${effort.date.format(DateTimeFormatter.ofPattern("MM/dd (E)"))}",
                fontSize = 20.sp
            )
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "${effort.startTime} ~ ")
                Text(text = "${effort.endTime}")
                Text(text = "(${effort.workingTime()}H)")
            }
        }
    }
}