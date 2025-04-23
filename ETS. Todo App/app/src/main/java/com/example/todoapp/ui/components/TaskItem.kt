package com.example.todoapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.Task
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TaskItem(
    task: Task,
    onTaskChecked: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onTaskChecked(task.id) },
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Deadline: ${task.deadline}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (task.deadline < currentDateTime && !task.isCompleted) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            IconButton(onClick = { onDelete(task.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete task")
            }
        }
    }
}