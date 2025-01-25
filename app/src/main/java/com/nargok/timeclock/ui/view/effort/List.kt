package com.nargok.timeclock.ui.view.effort

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nargok.timeclock.domain.model.EffortModel
import com.nargok.timeclock.navigation.Screen
import com.nargok.timeclock.viewmodel.effort.EffortListViewModel
import com.nargok.timeclock.navigation.navigateToEffortEdit
import com.nargok.timeclock.navigation.navigateToEffortRegister
import com.nargok.timeclock.navigation.navigateToStandardWorkingHourEdit
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffortListScreen(
    navController: NavController,
    success: Boolean = false,
    viewModel: EffortListViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val monthlyEffort by viewModel.monthlyEfforts
    val snackBarHostState = remember { SnackbarHostState() }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 100f
    var isAnimating by remember { mutableStateOf(false) }
    var swipeDirection by remember { mutableFloatStateOf(0f) }

    val offsetAnimation by animateFloatAsState(
        targetValue = if (isAnimating) swipeDirection * 1000f else 0f,
        animationSpec = tween(300),
        finishedListener = {
            if (isAnimating) {
                offsetX = 0f
                isAnimating = false
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.fetchMonthlyEfforts()
    }

    LaunchedEffect(success) {
        if (success) {
            snackBarHostState.showSnackbar(
                message = "作業時間を更新しました。",
                duration = SnackbarDuration.Short
            )
            // スナックバー表示後、URLパラメータをリセット
            navController.navigate(Screen.EffortList.createRoute(false)) {
                popUpTo(Screen.EffortList.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.clickable {
                            viewModel.toggleDisplayEffortSummary(!uiState.displayEffortSummary)
                        }
                    ) {
                        Text("${uiState.selectedYearMonth.year}/${uiState.selectedYearMonth.monthValue}(Avg: ${monthlyEffort?.averageWorkingHours()}H)")
                        if (uiState.displayEffortSummary) {
                            Icon(
                                Icons.Default.ArrowDropUp,
                                contentDescription = "Display monthly effort summary"
                            )

                        } else {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Display monthly effort summary"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigateToStandardWorkingHourEdit(uiState.selectedYearMonth)
                    }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Go to Standard working hours"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigateToEffortRegister() }) {
                Icon(Icons.Filled.Add, contentDescription = "")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .offset { IntOffset(offsetAnimation.roundToInt(), 0) }
                .pointerInput(Unit) {
                    // 横スワイプで前月・翌月に遷移
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when {
                                offsetX < -swipeThreshold -> {
                                    swipeDirection = -1f
                                    viewModel.setNextYearMonth()
                                    isAnimating = true
                                }

                                offsetX > swipeThreshold -> {
                                    swipeDirection = 1f
                                    viewModel.setPreviousYearMonth()
                                    isAnimating = true
                                }
                            }
                            offsetX = 0f
                        },
                        onDragCancel = { offsetX = 0f },
                        onHorizontalDrag = { _, dragAmount ->
                            if (!isAnimating) {
                                offsetX += dragAmount
                            }
                        }
                    )
                }
        ) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                if (uiState.displayEffortSummary) {
                    Text(
                        "基準時間: ${viewModel.standardWorkingHour.value}時間(${monthlyEffort?.totalDays()}日)",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        fontSize = 24.sp
                    )
                    Text(
                        "合計時間: ${monthlyEffort?.totalWorkingHours()}時間(${monthlyEffort?.workedDays()}日)",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        fontSize = 24.sp
                    )
                    Text(
                        "残り日数: ${monthlyEffort?.remainingDays()}日",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        fontSize = 24.sp
                    )
                    Text(
                        "平均: ${monthlyEffort?.averageWorkingHours()}H",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        fontSize = 24.sp
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                items(monthlyEffort?.effortList() ?: emptyList()) { effort ->
                    EffortListItem(effort) {
                        navController.navigateToEffortEdit(effort.id)
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
                text = "${effort.date.format(DateTimeFormatter.ofPattern("MM/dd (E)"))} ${if (effort.leave) "休" else ""}",
                fontSize = 20.sp
            )
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "${effort.startTime} ~ ")
                Text(text = "${effort.endTime}")
                Text(text = "(${effort.workingTime()}H)")
            }
        }
    }

    fun itemTitle(): String {
        return "${effort.date.format(DateTimeFormatter.ofPattern("MM/dd (E)"))}"
    }
}