package com.example.habitchecklist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitchecklist.domain.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    vm: HabitViewModel,
    navController: NavController
) {
    var text by remember { mutableStateOf("") }

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

            Button(
                onClick = {
                    vm.addHabit(text)
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
