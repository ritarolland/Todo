package com.example.todo.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.example.todo.ApiService
import com.example.todo.AuthInterceptor
import com.example.todo.CustomHostnameVerifier
import com.example.todo.LastKnownRevisionInterceptor
import com.example.todo.ServerTodoItemMapper
import com.example.todo.data.DeviceNameRepository
import com.example.todo.data.LastKnownRevisionRepository
import com.example.todo.data.TodoDatabase
import com.example.todo.data.TodoItemsRepository
import com.example.todo.data.TodoItemsRepositoryImpl
import com.example.todo.domain.models.TodoItemDao
import com.example.todo.utils.NetworkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import retrofit2.converter.scalars.ScalarsConverterFactory

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
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkChecker(context)
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
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun provideDeviceNameRepository(contentResolver: ContentResolver): DeviceNameRepository {
        return DeviceNameRepository(contentResolver)
    }

    @Provides
    fun provideLastKnownRevisionRepository(): LastKnownRevisionRepository {
        return LastKnownRevisionRepository()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        //val token = "Cerin"
        return AuthInterceptor()
    }



    @Provides
    @Singleton
    fun provideLastRevisionInterceptor(lastKnownRevisionRepository: LastKnownRevisionRepository): LastKnownRevisionInterceptor {
        //val token = "Cerin"
        return LastKnownRevisionInterceptor(lastKnownRevisionRepository)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor, lastKnownRevisionInterceptor: LastKnownRevisionInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(lastKnownRevisionInterceptor)
            .hostnameVerifier(CustomHostnameVerifier("beta.mrdekk.ru"))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todo/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTodoApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideServerTodoItemMapper(deviceNameRepository: DeviceNameRepository): ServerTodoItemMapper {
        return ServerTodoItemMapper(deviceNameRepository)
    }

}