package com.example.habitchecklist.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.habitchecklist.utils.FeedbackManager

@Composable
fun FeedbackDialog(
    onDismiss: () -> Unit,
    onFeedback: () -> Unit,
    onRating: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Оцените приложение",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Text(
                    text = "Помогите нам улучшить приложение! Оставьте отзыв или оценку.",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onFeedback()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Обратная связь")
                    }
                    
                    Button(
                        onClick = {
                            onRating()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Оценить")
                    }
                }
                
                TextButton(onClick = onDismiss) {
                    Text("Позже")
                }
            }
        }
    }
}

