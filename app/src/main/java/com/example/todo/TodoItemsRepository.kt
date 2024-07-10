package com.example.todo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class TodoItemsRepository {
    private val listOfToDo = mutableListOf<TodoItem>()

    init {
        listOfToDo.apply {
            add(
                TodoItem(
                    id = "1",
                    text = "Task 1",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "2",
                    text = "Task 2",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "3",
                    text = "Task 3",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = true,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "4",
                    text = "Task 4",
                    importance = Importance.HIGH,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "5",
                    text = "Task 5",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "6",
                    text = "Task 5",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "7",
                    text = "Task 5",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "8",
                    text = "Task 5",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "9",
                    text = "Task 5",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
            add(
                TodoItem(
                    id = "10",
                    text = "Task 5",
                    importance = Importance.NORMAL,
                    deadline = Date(),
                    isDone = false,
                    createdAt = Date()
                )
            )
        }
    }

    fun getToDoById(id: String?) = listOfToDo.firstOrNull { it.id == id }

    fun getAllToDo(): List<TodoItem> {
        return listOfToDo.toList()
    }

    fun addOrEditToDo(item: TodoItem) {
        if (item.id != null) {
            listOfToDo.replaceAll { if (it.id == item.id) item else it }
        } else {
            listOfToDo.add(0, item.copy(id = Calendar.getInstance().time.toString()))
        }
    }

    fun deleteToDo(item: TodoItem) {
        listOfToDo.remove(item)
    }

    fun filterTodos(showCompletedTasks: Boolean): List<TodoItem> {
        return if (showCompletedTasks) {
            listOfToDo
        } else {
            listOfToDo.filter { !it.isDone }
        }
    }

    fun checkItem(item: TodoItem, checked: Boolean) {
        val index = listOfToDo.indexOf(item)
        if (index != -1) {
            listOfToDo[index].isDone = checked
        }
    }


    fun countOfCompleted() = listOfToDo.count { it.isDone }
}