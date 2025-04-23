package com.example.todoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatListNumbered
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TaskViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    val tasks by viewModel.tasks.collectAsState()
    val sortType by viewModel.sortType.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Todo",
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { viewModel.setSortType(SortType.BY_DEADLINE) }) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Sort by deadline",
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
                            contentDescription = "Sort by status",
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
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
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
                            contentDescription = "No tasks",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            modifier = Modifier.size(120.dp)
                        )
                        Text(
                            "No tasks yet!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            "Tap + to add your first task",
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

