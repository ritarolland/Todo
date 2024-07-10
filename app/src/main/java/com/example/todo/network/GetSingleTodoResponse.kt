package com.example.todo.network

data class GetSingleTodoResponse(
    override val status: String,
    val element: ServerTodoItem,
    override val revision: Int
): TodoResponse