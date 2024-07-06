package com.example.todo.ui.mainScreen

import com.example.todo.domain.models.TodoItem

data class MainScreenUiModel(
    val tasks: List<TodoItem> = emptyList(),
    val completedTasksCount: Int = 0,
    val showCompletedTasks: Boolean = true,
)