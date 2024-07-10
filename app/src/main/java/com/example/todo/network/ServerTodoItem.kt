package com.example.todo.network

import com.google.gson.annotations.SerializedName

data class ServerTodoItem(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long?,
    val done: Boolean,
    val color: String?,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("changed_at")
    val changedAt: Long,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String
)