package com.example.habitchecklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.habitchecklist.domain.HabitViewModel
import com.example.habitchecklist.ui.navigation.NavRoutes
import com.example.habitchecklist.ui.screens.*
import com.example.habitchecklist.ui.theme.AppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val vm: HabitViewModel = viewModel()
            val navController = rememberNavController()

            AppTheme(context = this) {
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.LIST
                ) {
                    composable(NavRoutes.LIST) {
                        HabitListScreen(vm, navController)
                    }
                    composable(NavRoutes.ADD) {
                        AddHabitScreen(vm, navController)
                    }
                    composable(NavRoutes.SETTINGS) {
                        SettingsScreen(this@MainActivity)
                    }
                }
            }
        }
    }
}

