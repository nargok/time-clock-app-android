package com.example.timeclock.ui.view.effort

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.timeclock.domain.model.EffortModel
import com.example.timeclock.viewmodel.effort.EffortListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortListScreen(
    navController: NavController,
    viewModel: EffortListViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val efforts by viewModel.efforts

    // TODO YearMonthが変更されたら再取得する
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
        // TODO ここにStatesを出したい。総作業時間、基準時間、平均作業時間
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            items(efforts) { effort ->
                EffortListItem(effort)
                Divider()
            }
        }
    }
}

@Composable
fun EffortListItem(effort: EffortModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Text(text = "日時: ${effort.date}")
        Text(text = "開始時刻: ${effort.startTime}")
        Text(text = "終了時刻: ${effort.endTime}")
    }
}