package com.example.habitchecklist.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.habitchecklist.domain.HabitViewModel
import com.example.habitchecklist.ui.components.HabitItem
import com.example.habitchecklist.ui.navigation.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    vm: HabitViewModel,
    navController: NavController
) {
    // 🔑 ВАЖНО: StateFlow -> State
    val habits by vm.habits.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Привычки") },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(NavRoutes.SETTINGS) }
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavRoutes.ADD) }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            items(
                items = habits,
                key = { habit -> habit.id }
            ) { habit ->
                HabitItem(
                    habit = habit,
                    onDone = { vm.markDone(habit) },
                    onDelete = { vm.delete(habit) }
                )
            }
        }
    }
}