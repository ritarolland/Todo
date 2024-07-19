package com.example.todo.di

import android.content.Context
import androidx.room.Room
import com.example.todo.data.LastKnownRevisionRepository
import com.example.todo.data.ListRepository
import com.example.todo.data.TodoDatabase
import com.example.todo.data.TodoItemsRepository
import com.example.todo.data.TodoItemsRepositoryImpl
import com.example.todo.domain.models.TodoItemDao
import com.example.todo.network.ApiService
import com.example.todo.network.ServerTodoItemMapper
import com.example.todo.utils.NetworkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    fun provideTodoItemDao(database: TodoDatabase): TodoItemDao {
        return database.todoItemDao()
    }

    @Provides
    fun provideTodoItemsRepository(
        todoItemDao: TodoItemDao,
        todoApiService: ApiService,
        networkChecker: NetworkChecker,
        serverTodoItemMapper: ServerTodoItemMapper,
        lastKnownRevisionRepository: LastKnownRevisionRepository
    ): TodoItemsRepository {
        return TodoItemsRepositoryImpl(
            todoItemDao,
            todoApiService,
            networkChecker,
            serverTodoItemMapper,
            lastKnownRevisionRepository
        )
    }

    @Provides
    fun provideListRepository(
        todoItemDao: TodoItemDao,
        todoApiService: ApiService,
        networkChecker: NetworkChecker,
        serverTodoItemMapper: ServerTodoItemMapper,
        lastKnownRevisionRepository: LastKnownRevisionRepository
    ): ListRepository {
        return ListRepository(
            todoItemDao,
            todoApiService,
            networkChecker,
            serverTodoItemMapper,
            lastKnownRevisionRepository
        )
    }
}