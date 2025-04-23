package com.example.todoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.components.TaskDialog
import com.example.todoapp.ui.components.TaskItem
import com.example.todoapp.ui.viewmodels.SortType
import com.example.todoapp.ui.viewmodels.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TaskViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    val tasks by viewModel.tasks.collectAsState()
    val sortType by viewModel.sortType.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo App") },
                actions = {
                    IconButton(onClick = { viewModel.setSortType(SortType.BY_DEADLINE) }) {
                        Icon(
                            Icons.Default.FormatListNumbered,
                            contentDescription = "Sort by deadline",
                            tint = if (sortType == SortType.BY_DEADLINE) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                    IconButton(onClick = { viewModel.setSortType(SortType.BY_STATUS) }) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Sort by status",
                            tint = if (sortType == SortType.BY_STATUS) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { padding ->
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tasks yet. Add your first task!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onTaskChecked = { taskId ->
                            viewModel.toggleTaskCompletion(taskId)
                        },
                        onDelete = { taskId ->
                            viewModel.deleteTask(taskId)
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        TaskDialog(
            onDismiss = { showDialog = false },
            onConfirm = { newTask ->
                viewModel.addTask(newTask)
            }
        )
    }
}