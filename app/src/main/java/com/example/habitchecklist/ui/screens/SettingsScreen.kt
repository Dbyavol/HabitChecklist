package com.example.habitchecklist.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habitchecklist.data.ThemePreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    context: Context
) {
    val darkTheme by ThemePreferences
        .isDark(context)
        .collectAsState(initial = false)

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Настройки") })
        }
    ) { padding ->

        Row(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Темная тема",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = darkTheme,
                onCheckedChange = { enabled ->
                    scope.launch {
                        ThemePreferences.setDark(context, enabled)
                    }
                }
            )
        }
    }
}

