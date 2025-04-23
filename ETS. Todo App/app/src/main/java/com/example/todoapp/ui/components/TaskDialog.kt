package com.example.todoapp.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.Task
import kotlinx.datetime.*
import kotlin.random.Random
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Get current date and time
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    // Initialize with explicit types
    var deadlineDate by remember {
        mutableStateOf<LocalDate>(currentDateTime.date)
    }

    var deadlineTime by remember {
        mutableStateOf<LocalTime>(
            currentDateTime.time
                .toSecondOfDay()
                .plus(3600) // Add 1 hour in seconds
                .toLocalTime()
        )
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun showDatePicker() {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                deadlineDate = LocalDate(year, (month + 1), day)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun showTimePicker() {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                deadlineTime = LocalTime(hour, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val deadline = LocalDateTime(
                        deadlineDate.year,
                        deadlineDate.monthNumber,
                        deadlineDate.dayOfMonth,
                        deadlineTime.hour,
                        deadlineTime.minute
                    )
                    onConfirm(
                        Task(
                            id = Random.nextInt(),
                            title = title,
                            description = description,
                            deadline = deadline
                        )
                    )
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add New Task") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title*") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showDatePicker() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("${deadlineDate.dayOfMonth}/${deadlineDate.monthNumber}/${deadlineDate.year}")
                    }

                    Button(
                        onClick = { showTimePicker() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("%02d:%02d".format(deadlineTime.hour, deadlineTime.minute))
                    }
                }
            }
        }
    )
}

// Extension function to convert seconds back to LocalTime
private fun Int.toLocalTime(): LocalTime {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    return LocalTime(hours % 24, minutes % 60)
}