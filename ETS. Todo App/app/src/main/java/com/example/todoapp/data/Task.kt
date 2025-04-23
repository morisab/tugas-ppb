package com.example.todoapp.data

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val deadline: LocalDateTime,
    val isCompleted: Boolean = false
)