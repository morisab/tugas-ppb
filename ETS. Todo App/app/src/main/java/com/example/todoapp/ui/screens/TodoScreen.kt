package com.example.todoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.components.TaskDialog
import com.example.todoapp.ui.components.TaskItem
import com.example.todoapp.ui.viewmodels.SortType
import com.example.todoapp.ui.viewmodels.TaskViewModel
import com.example.todoapp.R
import com.example.todoapp.data.Task
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TaskViewModel = viewModel()) {
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val sortType by viewModel.sortType.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            stringResource(R.string.screen_title_todo),
                            modifier = Modifier.padding(start = 16.dp),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (sortType) {
                                SortType.BY_DEADLINE -> stringResource(R.string.sort_by_deadline)
                                SortType.BY_STATUS -> stringResource(R.string.sort_by_status)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { viewModel.setSortType(SortType.BY_DEADLINE) }) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = stringResource(R.string.sort_deadline_desc),
                            tint = if (sortType == SortType.BY_DEADLINE) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                    }
                    IconButton(onClick = { viewModel.setSortType(SortType.BY_STATUS) }) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.sort_status_desc),
                            tint = if (sortType == SortType.BY_STATUS) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                            taskToEdit = null
                            showDialog = true
                          },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_task_desc))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_empty_tasks),
                            contentDescription = stringResource(R.string.empty_state_desc),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            modifier = Modifier.size(120.dp)
                        )
                        Text(
                            stringResource(R.string.empty_state_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            stringResource(R.string.empty_state_message),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onTaskChecked = viewModel::toggleTaskCompletion,
                            onEdit = { selectedTask ->
                                taskToEdit = selectedTask
                                showDialog = true
                            },
                            onDelete = { taskToConfirmDelete ->
                                taskToDelete = taskToConfirmDelete
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        TaskDialog(
            taskToEdit = taskToEdit,
            onDismiss = {
                showDialog = false
                taskToEdit = null
            },
            onConfirm = { task ->
                if (taskToEdit == null) {
                    viewModel.addTask(task)
                } else {
                    viewModel.updateTask(task)
                }
                showDialog = false
                taskToEdit = null
            }
        )
    }
    if (taskToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                taskToDelete = null
            },
            title = {
                Text(stringResource(R.string.dialog_confirm_delete_title))
            },
            text = {
                Text(stringResource(R.string.dialog_confirm_delete_message, taskToDelete?.title ?: ""))
            },
            confirmButton = {
                Button(
                    onClick = {
                        taskToDelete?.let { viewModel.deleteTask(it) }
                        taskToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.button_yes_delete))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        taskToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.button_cancel))
                }
            }
        )
    }
}

