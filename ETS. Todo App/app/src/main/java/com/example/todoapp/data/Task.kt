package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val deadline: String,
    val isCompleted: Boolean = false
) {
    fun deadlineToLocalDateTime(): LocalDateTime = deadline.toLocalDateTime()

    companion object {
        fun fromLocalDateTime(
            title: String,
            description: String,
            deadline: LocalDateTime
        ): Task {
            return Task(
                title = title,
                description = description,
                deadline = deadline.toString()
            )
        }
    }
}