package com.example.todoapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortType {
    BY_DEADLINE,
    BY_STATUS
}


class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()

    private val _sortType = MutableStateFlow(SortType.BY_DEADLINE)
    val sortType: StateFlow<SortType> = _sortType

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks: StateFlow<List<Task>> = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.BY_DEADLINE -> taskDao.getAllTasksSortedByDeadline()
                SortType.BY_STATUS -> taskDao.getAllTasksSortedByStatus()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun setSortType(type: SortType) {
        _sortType.value = type
    }

    fun addTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
    }

    fun toggleTaskCompletion(taskId: Int) = viewModelScope.launch {
        taskDao.getTaskById(taskId)?.let { task ->
            taskDao.update(task.copy(isCompleted = !task.isCompleted))
        }
    }
}