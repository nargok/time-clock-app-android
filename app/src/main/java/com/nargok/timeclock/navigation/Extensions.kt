package com.nargok.timeclock.navigation

import androidx.navigation.NavController
import com.nargok.timeclock.domain.model.vo.EffortId
import java.time.YearMonth

fun NavController.navigateToEffortList(success: Boolean = false) {
    navigate(Screen.EffortList.route + if (success) "?success=$success" else "") {
        popUpTo(Screen.EffortList.route) { inclusive = true }
    }
}

fun NavController.navigateToEffortRegister() {
    navigate(Screen.EffortRegister.route)
}

fun NavController.navigateToEffortEdit(effortId: EffortId) {
    navigate(Screen.EffortEdit.createRoute(effortId.value))
}

fun NavController.navigateToStandardWorkingHourEdit(yearMonth: YearMonth) {
    navigate(Screen.StandardWorkingHourEdit.createRoute(yearMonth.toString()))
}
