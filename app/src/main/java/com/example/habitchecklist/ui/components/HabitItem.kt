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
    val targetColor = when (habit.state) {
        0 -> Color(0xFFA5D6A7)
        1 -> Color(0xFFFFF59D)
        else -> Color(0xFFEF9A9A)
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
                        Text(habit.name, fontSize = 18.sp)
                        Text("Streak: ${habit.streak}")
                    }
                    Button(onClick = onDone) { Text("✓") }
                }
            }
        }
    )
}

