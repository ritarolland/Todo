package com.example.todo.data

import android.util.Log
import com.example.todo.ApiService
import com.example.todo.NetworkException
import com.example.todo.ServerTodoItemMapper
import com.example.todo.TodoResponse
import com.example.todo.UpdateSingleTodoRequest
import com.example.todo.domain.models.TodoItem
import com.example.todo.domain.models.TodoItemDao
import com.example.todo.utils.NetworkChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject


interface TodoItemsRepository {
    fun getAllTodoItems(): Flow<List<TodoItem>>
    suspend fun updateChecked(id: String, isDone: Boolean)
    suspend fun getToDoById(id: String): TodoItem
    suspend fun addOrEditToDo(item: TodoItem)
    suspend fun deleteToDo(id: String)
}

class TodoItemsRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val todoApiService: ApiService,
    private val networkChecker: NetworkChecker,
    private val serverTodoItemMapper: ServerTodoItemMapper,
    private val lastKnownRevisionRepository: LastKnownRevisionRepository
) : TodoItemsRepository {

    val listOfToDo: Flow<List<TodoItem>> = getAllTodoItems()

    private suspend fun getItemsByNetwork(): List<TodoItem> {
        return handle {
            todoApiService.getAll()
        }.list.map { serverTodoItemMapper.mapTo(it) }
    }

    override fun getAllTodoItems(): Flow<List<TodoItem>> = flow {
        if (networkChecker.isNetworkAvailable()) {

            lateinit var remoteItems: List<TodoItem>
            val response = handle {
                todoApiService.getAll()
            }
            remoteItems = response.list.map { serverTodoItemMapper.mapTo(it) }
            //= getItemsByNetwork()
            todoItemDao.insertAll(remoteItems)
        }
        emitAll(todoItemDao.getAll())
        /*
                try {
                    val remoteTodos = todoApiService.getAll()
                    todoItemDao.insertAll(remoteTodos)
                    emit(remoteTodos)
                } catch (e: Exception) {
                    emitAll(todoItemDao.getAll())
                }*/
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
            // Проверяем доступность сети перед выполнением запроса
            if (networkChecker.isNetworkAvailable()) {
                // Выполняем запрос
                val response = todoApiService.addToDoItem(
                    UpdateSingleTodoRequest(
                        serverTodoItemMapper.mapFrom(item)
                    )
                )
                val body = response.body()
                // Проверяем код состояния HTTP
                if (response.isSuccessful && body!= null) {
                    // Обработка успешного ответа
                    Log.d(TAG, "upsertTodo успешно выполнен, код состояния ${response.code()}")
                    lastKnownRevisionRepository.updateRevision(body.revision)
                    // Дополнительная логика при успешном запросе
                } else {
                    // Обработка ошибки
                    Log.e(TAG, "Ошибка при выполнении upsertTodo, код состояния ${response.code()}")
                    // Обработка ошибки, например, вывод сообщения об ошибке
                }
            } else {
                // Сеть недоступна
                Log.e(TAG, "Сеть недоступна при выполнении upsertTodo")
                // Обработка ситуации, когда сеть недоступна
            }
        } catch (e: Exception) {
            // Обработка других ошибок, например, IOException, JsonParseException и других
            Log.e(TAG, "Ошибка при выполнении upsertTodo: ${e.message}", e)
            // Обработка ошибки, например, вывод сообщения об ошибке
        }
        todoItemDao.upsert(item)


        /*try {
            val response: Response<TodoItem> = todoApiService.upsertTodo(item.id, item)
            if (response.isSuccessful) {
                response.body()?.let { updatedItem ->
                    todoItemDao.upsert(updatedItem)
                }
            } else {
                todoItemDao.upsert(item)
            }
        } catch (e: Exception) {
            todoItemDao.upsert(item)
        }
*/
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