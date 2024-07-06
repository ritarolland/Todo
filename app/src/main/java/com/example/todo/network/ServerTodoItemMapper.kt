package com.example.todo.network

import com.example.todo.data.DeviceNameRepository
import com.example.todo.domain.models.TodoItem
import com.example.todo.utils.DateConverter
import com.example.todo.utils.ImportanceConverter
import javax.inject.Inject

class ServerTodoItemMapper @Inject constructor(
    private val deviceNameRepository: DeviceNameRepository
) {
    fun mapTo(cloudModel: ServerTodoItem): TodoItem {
        return TodoItem(
            id = cloudModel.id,
            text = cloudModel.text,
            importance = ImportanceConverter.stringToImportance(cloudModel.importance),
            deadline = cloudModel.deadline?.let { DateConverter.timestampToDate(it) },
            isDone = cloudModel.done,
            createdAt = DateConverter.timestampToDate(cloudModel.createdAt),
            updatedAt = DateConverter.timestampToDate(cloudModel.changedAt)
        )
    }

    fun mapFrom(todoItem: TodoItem): ServerTodoItem {
        return ServerTodoItem(
            id = todoItem.id,
            text = todoItem.text,
            importance = ImportanceConverter.importanceToString(todoItem.importance),
            deadline = todoItem.deadline?.let { DateConverter.dateToTimestamp(it) },
            done = todoItem.isDone,
            color = null,
            createdAt = DateConverter.dateToTimestamp(todoItem.createdAt),
            changedAt = if (todoItem.updatedAt == null) DateConverter.dateToTimestamp(todoItem.createdAt)
            else DateConverter.dateToTimestamp(todoItem.updatedAt),
            lastUpdatedBy = deviceNameRepository.getDeviceName()
        )
    }
}