package com.example.todo

class UpdateTodoListRequest(
    val status: String,
    val list: List<ServerTodoItem>
)