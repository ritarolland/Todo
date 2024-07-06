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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoItemsRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val todoApiService: ApiService,
    private val networkChecker: NetworkChecker,
    private val serverTodoItemMapper: ServerTodoItemMapper,
    private val lastKnownRevisionRepository: LastKnownRevisionRepository
) : TodoItemsRepository {

    val listOfToDo: Flow<List<TodoItem>> = getAllTodoItems()


    override fun getAllTodoItems(): Flow<List<TodoItem>> = flow {
        if (networkChecker.isNetworkAvailable()) {

            val response = handle {
                todoApiService.getAll()
            }
            val remoteItems = response.list.map { serverTodoItemMapper.mapTo(it) }
            todoItemDao.insertAll(remoteItems)
            lastKnownRevisionRepository.updateRevision(response.revision)
        }
        emitAll(todoItemDao.getAll())
    }

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
                val response = todoApiService.addToDoItem(
                    UpdateSingleTodoRequest(
                        serverTodoItemMapper.mapFrom(item)
                    )
                )
                val body = response.body()
                if (response.isSuccessful && body!= null) {
                    lastKnownRevisionRepository.updateRevision(body.revision)
                }
            }
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