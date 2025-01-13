package com.nargok.timeclock.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nargok.timeclock.domain.model.vo.EffortId
import com.nargok.timeclock.ui.view.effort.EffortEditScreen
import com.nargok.timeclock.ui.view.effort.EffortListScreen
import com.nargok.timeclock.ui.view.effort.EffortRegisterScreen
import com.nargok.timeclock.ui.view.standard_working_hours.StandardWorkingHourEditScreen
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeClockNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.EffortList.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // 作業時間リスト
        composable(
            route = Screen.EffortList.route,
            arguments = listOf(
                navArgument("success") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val success = backStackEntry.arguments?.getBoolean("success") ?: false
            EffortListScreen(navController = navController, success = success)
        }

        // 作業時間登録画面
        composable(route = Screen.EffortRegister.route) {
            EffortRegisterScreen(
                navController = navController,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.navigate(Screen.EffortList.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }

        // 作業編集画面
        composable(
            route = Screen.EffortEdit.route,
            arguments = listOf(
                navArgument("effortId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val effortId = backStackEntry.arguments?.getString("effortId")
            requireNotNull(effortId) { "effortId is required" }

            EffortEditScreen(
                navController = navController,
                id = EffortId.reconstruct(effortId),
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.navigate(Screen.EffortList.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }

        // 標準作業時間編集画面
        composable(
            route = Screen.StandardWorkingHourEdit.route,
            arguments = listOf(
                navArgument("yearMonth") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val yearMonth = backStackEntry.arguments?.getString("yearMonth")
            requireNotNull(yearMonth) { "yearMonth is required" }

            StandardWorkingHourEditScreen(
                navController = navController,
                yearMonth = YearMonth.parse(yearMonth),
            )
        }
    }
}
