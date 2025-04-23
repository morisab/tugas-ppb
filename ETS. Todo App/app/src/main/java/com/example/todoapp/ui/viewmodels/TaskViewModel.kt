package com.example.todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlin.random.Random

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _sortType = MutableStateFlow(SortType.BY_DEADLINE)
    val sortType: StateFlow<SortType> = _sortType

    fun addTask(task: Task) {
        viewModelScope.launch {
            _tasks.update { currentTasks ->
                currentTasks + task
            }
            sortTasks()
        }
    }

    fun toggleTaskCompletion(taskId: Int) {
        viewModelScope.launch {
            _tasks.update { currentTasks ->
                currentTasks.map { task ->
                    if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
                }
            }
            sortTasks()
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            _tasks.update { currentTasks ->
                currentTasks.filterNot { it.id == taskId }
            }
        }
    }

    fun setSortType(type: SortType) {
        viewModelScope.launch {
            _sortType.update { type }
            sortTasks()
        }
    }

    private fun sortTasks() {
        viewModelScope.launch {
            _tasks.update { currentTasks ->
                when (_sortType.value) {
                    SortType.BY_DEADLINE -> currentTasks.sortedBy { it.deadline }
                    SortType.BY_STATUS -> currentTasks.sortedWith(
                        compareBy<Task> { it.isCompleted }.thenBy { it.deadline }
                    )
                }
            }
        }
    }
}

enum class SortType {
    BY_DEADLINE, BY_STATUS
}