package com.example.habitchecklist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitchecklist.domain.HabitViewModel
import com.example.habitchecklist.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    vm: HabitViewModel,
    navController: NavController
) {
    var text by remember { mutableStateOf("") }
    var reminderEnabled by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(DateUtils.formatTime(8 * 60)) }
    var endTime by remember { mutableStateOf(DateUtils.formatTime(22 * 60)) }
    var remindersPerDay by remember { mutableStateOf(1f) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Новая привычка") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Название привычки") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Напоминания", modifier = Modifier.weight(1f))
                Switch(
                    checked = reminderEnabled,
                    onCheckedChange = { reminderEnabled = it }
                )
            }

            if (reminderEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Начало окна (HH:MM)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("Конец окна (HH:MM)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Количество напоминаний: ${remindersPerDay.toInt()}")
                Slider(
                    value = remindersPerDay,
                    onValueChange = { remindersPerDay = it },
                    valueRange = 1f..5f,
                    steps = 3
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val startMinutes = DateUtils.parseTime(startTime) ?: 8 * 60
                    val endMinutes = DateUtils.parseTime(endTime) ?: 22 * 60
                    val normalizedEnd = if (endMinutes > startMinutes) endMinutes else startMinutes + 60

                    val remindersCount = if (reminderEnabled) remindersPerDay.toInt() else 0

                    vm.addHabit(
                        name = text,
                        reminderEnabled = reminderEnabled,
                        windowStartMinutes = startMinutes,
                        windowEndMinutes = normalizedEnd,
                        remindersPerDay = remindersCount
                    )
                    navController.popBackStack()
                },
                enabled = text.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Создать")
            }
        }
    }
}
