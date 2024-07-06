package com.example.todo.domain.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items")
    fun getAll(): Flow<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TodoItem>)

    @Query("UPDATE todo_items SET isDone = :isDone WHERE id = :id")
    suspend fun updateChecked(id: String, isDone: Boolean)

    @Query("SELECT * FROM todo_items WHERE id = :id LIMIT 1")
    suspend fun getToDoById(id: String): TodoItem

    @Upsert
    suspend fun upsert(todoItem: TodoItem)

    @Delete
    suspend fun delete(todoItem: TodoItem)

    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteToDoById(id: String)
}