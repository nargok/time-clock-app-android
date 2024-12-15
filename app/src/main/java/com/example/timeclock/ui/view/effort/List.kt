package com.example.timeclock.ui.view.effort

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.timeclock.viewmodel.EffortViewModel
import com.example.timeclock.domain.model.EffortModel
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortListScreen(
    navController: NavController,
    viewModel: EffortViewModel = hiltViewModel()
) {
    // TODO get it from viewmodel
    val dummyDate = LocalDate.now()
    val dummyStartTime = LocalTime.parse("09:00")
    val dummyEndTime = LocalTime.parse("18:00")
    val efforts: List<EffortModel> = listOf<EffortModel>(
        EffortModel.create(dummyDate, dummyStartTime, dummyEndTime),
        EffortModel.create(dummyDate.plusDays(1), dummyStartTime, dummyEndTime),
        EffortModel.create(dummyDate.plusDays(2), dummyStartTime, dummyEndTime),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Effort List") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("effortRegister") }) {
                Icon(Icons.Filled.Add, contentDescription = "")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) { items(efforts) { effort ->
            EffortListItem(effort)
            Divider()
        } }

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