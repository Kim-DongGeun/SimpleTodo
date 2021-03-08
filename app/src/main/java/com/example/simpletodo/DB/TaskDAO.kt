package com.example.simpletodo.DB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {

    @Insert
    suspend fun insertTask(task: Task) : Long

    @Update
    suspend fun updateTask(task: Task) : Int

    @Delete
    suspend fun deleteTask(task: Task) : Int

    @Query("DELETE FROM task_table")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM task_table")
    fun getAllTask() : LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE task_iscomplete = 0")
    fun getUnCompletedTask() : LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE task_iscomplete = 1")
    fun getCompletedTask() : LiveData<List<Task>>

}