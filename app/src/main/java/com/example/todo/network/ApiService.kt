package com.example.todo.network

import com.example.todo.domain.models.TodoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("list")
    suspend fun getAll(): Response<GetTodoListResponse>

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") revision: String,
        @Body items: List<TodoItem>
    ): Response<GetTodoListResponse>

    @GET("list/{id}")
    suspend fun getToDoItemById(@Path("id") id: String): Response<GetSingleTodoResponse>

    @POST("list")
    suspend fun addToDoItem(@Body request: UpdateSingleTodoRequest): Response<GetSingleTodoResponse>

    @PUT("list/{id}")
    suspend fun upsertTodo(
        @Path("id") id: String,
        @Body request: UpdateSingleTodoRequest
    ): Response<GetSingleTodoResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodo(@Path("id") id: String): Response<GetTodoListResponse>
}