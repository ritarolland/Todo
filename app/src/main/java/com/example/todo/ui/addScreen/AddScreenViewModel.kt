package com.example.todo.ui.addScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TodoItemsRepository
import com.example.todo.domain.models.Importance
import com.example.todo.domain.models.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository
) : ViewModel() {

    private val _todoItem = MutableStateFlow(TodoItem())
    val todoItem = _todoItem.asStateFlow()

    private val _errorFlow = MutableStateFlow<String?>(null)
    val errorFlow = _errorFlow.asStateFlow()

    private fun runSafeInBackground(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                block.invoke()
            } catch (e: Exception) {
                _errorFlow.value = e.message ?: "Something went wrong"
            }
        }
    }

    fun updateText(text: String) {
        runSafeInBackground {
            _todoItem.value = todoItem.value.copy(
                text = text
            )
        }
    }

    fun updateDeadline(date: Date?) {
        runSafeInBackground {
            _todoItem.value = todoItem.value.copy(
                deadline = date
            )
        }
    }

    fun updateImportance(importance: Importance) {
        runSafeInBackground {
            _todoItem.value = todoItem.value.copy(
                importance = importance
            )
        }
    }

    fun getItemById(id: String?) {
        runSafeInBackground {
            if (id != null) {
                _todoItem.value = repository.getToDoById(id)
            } else _todoItem.value = TodoItem()
        }
    }

    fun saveItem() {
        runSafeInBackground {
            repository.addOrEditToDo(
                TodoItem(
                    id = todoItem.value.id.ifEmpty {
                        UUID.randomUUID().toString()
                    },
                    text = todoItem.value.text,
                    deadline = todoItem.value.deadline,
                    importance = todoItem.value.importance,
                    isDone = if (todoItem.value.id.isEmpty()) {
                        false
                    } else {
                        todoItem.value.isDone
                    },
                    createdAt = if (todoItem.value.id.isEmpty()) {
                        Date()
                    } else {
                        todoItem.value.createdAt
                    },
                    updatedAt = if (todoItem.value.id.isEmpty()) {
                        null
                    } else {
                        Date()
                    }
                )
            )
        }
    }

    fun deleteItem() {
        runSafeInBackground {
            repository.deleteToDo(todoItem.value.id)
        }
    }

    fun clearError() {
        _errorFlow.value = null
    }

}