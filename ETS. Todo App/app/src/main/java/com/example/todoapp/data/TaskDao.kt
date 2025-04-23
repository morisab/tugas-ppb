package com.example.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    fun getAllTasksSortedByDeadline(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC")
    fun getAllTasksSortedByStatus(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
}