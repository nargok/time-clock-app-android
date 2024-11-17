package com.example.timeclock.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EffortRegisterScreen() {
    var date by remember { mutableStateOf("") }
    var startHour by remember { mutableStateOf("") }
    var endHour by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("日付") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = startHour,
            onValueChange = { startHour = it },
            label = { Text("開始") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = endHour,
            onValueChange = { endHour = it },
            label = { Text("終了") },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(onClick = {
            println("""
                日付: $date
                開始: $startHour
                終了: $endHour
            """.trimIndent())
        }) {
            Text("保存")
        }
    }


}