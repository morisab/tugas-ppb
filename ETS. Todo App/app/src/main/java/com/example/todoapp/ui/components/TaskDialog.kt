package com.example.todoapp.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todoapp.data.Task
import kotlinx.datetime.*
import kotlin.random.Random
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    var deadlineDate by remember {
        mutableStateOf(currentDateTime.date)
    }

    var deadlineTime by remember {
        mutableStateOf(
            currentDateTime.time
                .toSecondOfDay()
                .plus(3600) // Add 1 hour in seconds
                .toLocalTime()
        )
    }

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Create New Task",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title*") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    maxLines = 4
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    deadlineDate = LocalDate(year, (month + 1), day)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                        modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                        border = ButtonDefaults.outlinedButtonBorder,
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Select date",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "${deadlineDate.dayOfMonth}/${deadlineDate.monthNumber}/${deadlineDate.year % 100}",
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    deadlineTime = LocalTime(hour, minute)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        },
                        modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                        border = ButtonDefaults.outlinedButtonBorder,
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Select time",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "%02d:%02d".format(deadlineTime.hour, deadlineTime.minute),
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("Cancel")
                    }

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
                            onDismiss()
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
}

private fun Int.toLocalTime(): LocalTime {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    return LocalTime(hours % 24, minutes % 60)
}


