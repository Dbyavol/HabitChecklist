package com.example.habitchecklist.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitchecklist.data.ThemePreferences
import com.example.habitchecklist.ui.components.FeedbackDialog
import com.example.habitchecklist.utils.AppMetrics
import com.example.habitchecklist.utils.FeedbackManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    context: Context,
    navController: NavController
) {
    val darkTheme by ThemePreferences
        .isDark(context)
        .collectAsState(initial = false)

    val scope = rememberCoroutineScope()
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var showMetrics by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Темная тема
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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

            Divider()

            // Обратная связь
            ListItem(
                headlineContent = { Text("Обратная связь") },
                leadingContent = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    showFeedbackDialog = true
                }
            )

            Divider()

            // Метрики (для разработки)
            ListItem(
                headlineContent = { Text("Метрики приложения") },
                supportingContent = { 
                    if (showMetrics) {
                        Text(
                            text = AppMetrics.getMetricsReport(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                leadingContent = {
                    Icon(Icons.Default.Info, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    showMetrics = !showMetrics
                }
            )

            Divider()
        }
    }

    if (showFeedbackDialog) {
        FeedbackDialog(
            onDismiss = { showFeedbackDialog = false },
            onFeedback = {
                FeedbackManager.openFeedback(context)
                FeedbackManager.recordFeedbackPrompt()
            },
            onRating = {
                FeedbackManager.openRating(context)
                FeedbackManager.recordFeedbackPrompt()
            }
        )
    }
}

