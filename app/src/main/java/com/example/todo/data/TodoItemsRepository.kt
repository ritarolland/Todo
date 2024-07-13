package com.example.todo.data

import com.example.todo.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow


interface TodoItemsRepository {
    suspend fun updateChecked(id: String, isDone: Boolean)
    suspend fun getToDoById(id: String): TodoItem
    suspend fun addOrEditToDo(item: TodoItem)
    suspend fun deleteToDo(id: String)
}

