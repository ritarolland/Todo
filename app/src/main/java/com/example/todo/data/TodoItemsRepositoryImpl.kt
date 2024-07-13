package com.example.todo.data

import android.util.Log
import com.example.todo.domain.models.TodoItem
import com.example.todo.domain.models.TodoItemDao
import com.example.todo.network.ApiService
import com.example.todo.network.NetworkException
import com.example.todo.network.ServerTodoItemMapper
import com.example.todo.network.TodoResponse
import com.example.todo.network.UpdateSingleTodoRequest
import com.example.todo.utils.NetworkChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListRepository @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val todoApiService: ApiService,
    private val networkChecker: NetworkChecker,
    private val serverTodoItemMapper: ServerTodoItemMapper,
    private val lastKnownRevisionRepository: LastKnownRevisionRepository,
) {
    private val _listOfToDo = MutableStateFlow<List<TodoItem>>(emptyList())
    val listOfToDo = _listOfToDo.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            initializeData()
        }
    }

    private suspend fun initializeData() {
        if (networkChecker.isNetworkAvailable()) {
            val response = handle {
                todoApiService.getAll()
            }
            val remoteItems = response.list.map { serverTodoItemMapper.mapTo(it) }
            todoItemDao.insertAll(remoteItems)
            lastKnownRevisionRepository.updateRevision(response.revision)
        }

        todoItemDao.getAll().collect { todoItems ->
            _listOfToDo.value = todoItems
        }
    }

    private suspend fun <T : TodoResponse> handle(block: suspend () -> Response<T>): T {
        val response = block.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            lastKnownRevisionRepository.updateRevision(body.revision)
            return body
        } else {
            throw NetworkException(response.errorBody()?.string())
        }
    }
}

@Singleton
class TodoItemsRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val todoApiService: ApiService,
    private val networkChecker: NetworkChecker,
    private val serverTodoItemMapper: ServerTodoItemMapper,
    private val lastKnownRevisionRepository: LastKnownRevisionRepository,
) : TodoItemsRepository {

    /*override suspend fun getAllTodoItems(): Flow<List<TodoItem>> = flow {
        if (networkChecker.isNetworkAvailable()) {

            val response = handle {
                todoApiService.getAll()
            }
            val remoteItems = response.list.map { serverTodoItemMapper.mapTo(it) }
            todoItemDao.insertAll(remoteItems)
            lastKnownRevisionRepository.updateRevision(response.revision)
        }
        emitAll(todoItemDao.getAll())
    }*/

    override suspend fun updateChecked(id: String, isDone: Boolean) {
        todoItemDao.updateChecked(id, isDone)
    }

    override suspend fun getToDoById(id: String): TodoItem {
        return todoItemDao.getToDoById(id)
    }

    private val TAG = "AAA"

    override suspend fun addOrEditToDo(item: TodoItem) {
        try {
            if (networkChecker.isNetworkAvailable()) {
                Log.d(
                    TAG,
                    "Ревизия перед добавлением элемента ${lastKnownRevisionRepository.lastKnownRevision}"
                )
                val response = todoApiService.addToDoItem(
                    lastKnownRevisionRepository.lastKnownRevision.toString(),
                    UpdateSingleTodoRequest(
                        serverTodoItemMapper.mapFrom(item)
                    )
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    lastKnownRevisionRepository.updateRevision(body.revision)
                    Log.d(TAG, lastKnownRevisionRepository.lastKnownRevision.toString())
                }
            } else Log.d(TAG, "Error connection")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при выполнении upsertTodo: ${e.message}", e)
        }
        todoItemDao.upsert(item)
    }

    override suspend fun deleteToDo(id: String) {
        try {
            todoApiService.deleteTodo(id)
            todoItemDao.deleteToDoById(id)
        } catch (e: Exception) {
            todoItemDao.deleteToDoById(id)
        }
    }

    private suspend fun <T : TodoResponse> handle(block: suspend () -> Response<T>): T {
        val response = block.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            lastKnownRevisionRepository.updateRevision(body.revision)
            return body
        } else {
            throw NetworkException(response.errorBody()?.string())
        }
    }

}