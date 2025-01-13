package com.nargok.timeclock.navigation

sealed class Screen(val route: String) {
    object EffortList : Screen("effortList")
    object EffortRegister : Screen("effortRegister")
    object EffortEdit : Screen("effortEdit/{id}") {
        fun createRoute(effortId: String) = "effortEdit/$effortId"
    }
    object StandardWorkingHourEdit : Screen("standardWorkingHourEdit/{yearMonth}") {
        fun createRoute(yearMonth: String) = "standardWorkingHourEdit/$yearMonth"
    }
}