package com.example.todo.network

data class UpdateSingleTodoRequest(
    val element: ServerTodoItem
)