package com.example.todo.data/*
package com.example.homework1.data

import com.example.homework1.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoItemRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : TodoItemsRepository {

    override suspend fun getAllTodoItems(): Flow<List<TodoItem>> {
        return try {
            val tasks = remoteDataSource.getTasks()
            localDataSource.saveTasks(tasks)
            localDataSource.getAllTodoItems()
        } catch (e: Exception) {
            localDataSource.getAllTodoItems()
        }
    }

    override suspend fun addOrEditTodoItem(todoItem: TodoItem) {
        try {
            remoteDataSource.addOrEditTodoItem(todoItem)
            localDataSource.addOrEditTodoItem(todoItem)
        } catch (e: Exception) {
            localDataSource.addOrEditTodoItem(todoItem)
        }
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        try {
            remoteDataSource.deleteTodoItem(todoItem)
            localDataSource.deleteTodoItem(todoItem)
        } catch (e: Exception) {
            localDataSource.deleteTodoItem(todoItem)
        }
    }

    override suspend fun updateChecked(id: String, isDone: Boolean) {
        val todoItem = localDataSource.getTodoItemById(id)?.copy(isDone = isDone)
        todoItem?.let {
            addOrEditTodoItem(it)
        }
    }
}*/
