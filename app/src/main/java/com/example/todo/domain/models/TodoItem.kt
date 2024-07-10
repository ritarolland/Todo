package com.example.todo.domain.models

import androidx.room.Entity
import java.util.Date
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey
    val id: String = "",
    val text: String = "",
    val importance: Importance = Importance.NORMAL,
    val deadline: Date? = null,
    var isDone: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date? = null
)
