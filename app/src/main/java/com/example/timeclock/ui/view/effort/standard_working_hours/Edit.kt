package com.example.timeclock.ui.view.effort.standard_working_hours

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.YearMonth

@ExperimentalMaterial3Api
@Composable
fun StandardWorkingHourEdit(
    yearMonth: YearMonth,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("${yearMonth.year}年${yearMonth.monthValue}月の標準作業時間")
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("時間(H)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Button(onClick = {
                // TODO saveに成功・失敗したらMessageを表示
            }, modifier = Modifier.padding(16.dp)) {
                Text("保存")
            }
        }
    }
}