package com.example.habitchecklist

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.habitchecklist.domain.HabitViewModel
import com.example.habitchecklist.ui.navigation.NavRoutes
import com.example.habitchecklist.ui.screens.*
import com.example.habitchecklist.ui.theme.AppTheme
import com.example.habitchecklist.utils.AppMetrics
import com.example.habitchecklist.utils.FeedbackManager
import com.example.habitchecklist.utils.PermissionHelper


class MainActivity : ComponentActivity() {

    private var appStartTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        appStartTime = SystemClock.elapsedRealtime()
        
        // Инициализация систем мониторинга
        AppMetrics.initialize(this)
        FeedbackManager.initialize(this)
        AppMetrics.recordSession()
        
        // Запрос разрешения на уведомления (Android 13+)
        if (!PermissionHelper.hasNotificationPermission(this)) {
            PermissionHelper.requestNotificationPermission(this)
        }

        setContent {
            val vm: HabitViewModel = viewModel()
            val navController = rememberNavController()

            AppTheme(context = this) {
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.LIST
                ) {
                    composable(NavRoutes.LIST) {
                        HabitListScreen(vm, navController, this@MainActivity)
                    }
                    composable(NavRoutes.ADD) {
                        AddHabitScreen(vm, navController)
                    }
                    composable(NavRoutes.SETTINGS) {
                        SettingsScreen(this@MainActivity, navController)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val startTime = SystemClock.elapsedRealtime() - appStartTime
        AppMetrics.recordAppStartTime(startTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Можно добавить логику сохранения состояния
    }
}

