package com.example.taskapp.data.local.dataBase

import androidx.room.*
import com.example.taskapp.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    //Сортировка title от А до Я ASC
    //Сортировка title от Я до А DESC
    @Query("SELECT * FROM task ORDER BY title ASC")
    fun getAll(): List<Task>

}

