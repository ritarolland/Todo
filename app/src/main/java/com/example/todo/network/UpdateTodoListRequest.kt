package com.example.todo.network

class UpdateTodoListRequest(
    val status: String,
    val list: List<ServerTodoItem>
)