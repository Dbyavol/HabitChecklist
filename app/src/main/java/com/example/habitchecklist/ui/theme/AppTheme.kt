package com.example.habitchecklist.ui.theme

import android.content.Context
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.habitchecklist.data.ThemePreferences

@Composable
fun AppTheme(
    context: Context,
    content: @Composable () -> Unit
) {
    val darkTheme by ThemePreferences
        .isDark(context)
        .collectAsState(initial = false)

    MaterialTheme(
        colorScheme = if (darkTheme) {
            darkColorScheme()
        } else {
            lightColorScheme()
        },
        content = content
    )
}
