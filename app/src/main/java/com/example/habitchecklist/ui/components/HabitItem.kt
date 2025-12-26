package com.example.habitchecklist.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitchecklist.data.Habit




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitItem(
    habit: Habit,
    onDone: () -> Unit,
    onDelete: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    // Определяем темную тему по яркости цвета поверхности
    // Вычисляем яркость по формуле: 0.299*R + 0.587*G + 0.114*B
    val surfaceColor = colorScheme.surface
    val brightness = (surfaceColor.red * 0.299f + surfaceColor.green * 0.587f + surfaceColor.blue * 0.114f)
    val isDark = brightness < 0.5f
    
    // Адаптивные цвета для светлой и темной темы
    val targetColor = when (habit.state) {
        0 -> if (isDark) Color(0xFF2E7D32) else Color(0xFFA5D6A7) // Зеленый
        1 -> if (isDark) Color(0xFFF57F17) else Color(0xFFFFF59D) // Желтый
        else -> if (isDark) Color(0xFFC62828) else Color(0xFFEF9A9A) // Красный
    }
    
    // Цвет текста для лучшей читаемости
    val textColor = if (isDark) {
        Color.White
    } else {
        Color.Black
    }

    val bgColor by animateColorAsState(targetValue = targetColor, label = "color")

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                onDelete()
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = habit.name, 
                            fontSize = 18.sp,
                            color = textColor
                        )
                        Text(
                            text = "Streak: ${habit.streak}",
                            color = textColor.copy(alpha = 0.8f)
                        )
                    }
                    Button(onClick = onDone) { 
                        Text("✓", color = Color.White) 
                    }
                }
            }
        }
    )
}

