package com.example.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel : ViewModel() {

    private val repository = TodoItemsRepository()
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> get() = _todos
    private val _completedTasksCount = MutableStateFlow<Int>(0)
    val completedTasksCount: StateFlow<Int> get() = _completedTasksCount
    private val _errorFlow = MutableStateFlow<String?>(null)
    val errorFlow: StateFlow<String?> get() = _errorFlow

    private var _showCompletedTasks = MutableStateFlow(true)


    val showCompletedTasks: StateFlow<Boolean>
        get() = _showCompletedTasks


    init {
        updateTodos()
        updateCompletedTasksCount()
    }

    private fun runSafeInBackground(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {        try {
            block.invoke()        } catch (e: Exception) {
            _errorFlow.value = e.message ?: "something went wrong"}
        }}


    fun loadTodos() {
        runSafeInBackground {
            _todos.value = repository.getAllToDo()
        }
    }



    fun getItemById(id : String) : TodoItem? {
        return repository.getToDoById(id)
    }

    fun addOrEditTodoItem(todoItem: TodoItem) {
        runSafeInBackground {
            repository.addOrEditToDo(todoItem)
            updateTodos()
            updateCompletedTasksCount()
        }
    }

    fun removeTodoItem(todoItem: TodoItem) {
        runSafeInBackground {
            repository.deleteToDo(todoItem)
            updateTodos()
            updateCompletedTasksCount()
        }

    }
    private fun updateCompletedTasksCount() {
        runSafeInBackground {
            val count = repository.countOfCompleted()
            _completedTasksCount.value = count
        }
    }

    fun toggleShowCompletedTasks() {
        _showCompletedTasks.value = !_showCompletedTasks.value!!
        updateTodos()
    }

    fun updateTodos() {
        runSafeInBackground {
            _todos.value = repository.filterTodos(_showCompletedTasks.value)
        }
    }
}
