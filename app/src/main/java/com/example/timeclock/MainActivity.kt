package com.example.timeclock

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.timeclock.domain.model.vo.EffortId
import com.example.timeclock.ui.theme.TimeClockTheme
import com.example.timeclock.ui.view.effort.EffortEditScreen
import com.example.timeclock.ui.view.effort.EffortListScreen
import com.example.timeclock.ui.view.effort.EffortRegisterScreen
import com.example.timeclock.ui.view.effort.standard_working_hours.StandardWorkingHourEdit
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.time.YearMonth

@HiltAndroidApp
class TimeClockApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeClockTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "effortList") {
                        composable("effortList") { EffortListScreen(navController) }
                        composable("effortRegister") { EffortRegisterScreen(navController) }
                        composable(
                            "effortEdit/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.let {
                                EffortId.reconstruct(it)
                            } ?: return@composable
                            EffortEditScreen(id, navController)
                        }
                        composable(
                            "standardWorkingHourEdit/{yearMonth}",
                            arguments = listOf(navArgument("yearMonth") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val yearMonth = backStackEntry.arguments?.getString("yearMonth")?.let {
                                YearMonth.parse(it)
                            } ?: return@composable
                            StandardWorkingHourEdit(yearMonth, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TimeClockTheme {
        Greeting("Android")
    }
}