package com.example.todo.di

import android.content.ContentResolver
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.room.Room
import com.example.todo.network.ApiService
import com.example.todo.network.interceptors.AuthInterceptor
import com.example.todo.network.CustomHostnameVerifier
import com.example.todo.network.ServerTodoItemMapper
import com.example.todo.data.DeviceNameRepository
import com.example.todo.data.LastKnownRevisionRepository
import com.example.todo.data.ListRepository
import com.example.todo.data.TodoDatabase
import com.example.todo.data.TodoItemsRepository
import com.example.todo.data.TodoItemsRepositoryImpl
import com.example.todo.di.app.AppScope
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

    /*@Provides
    @AppScope
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkChecker(context)
    }*/
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

    /*@Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun provideDeviceNameRepository(contentResolver: ContentResolver): DeviceNameRepository {
        return DeviceNameRepository(contentResolver)
    }

    @Provides
    @AppScope
    fun provideLastKnownRevisionRepository(): LastKnownRevisionRepository {
        return LastKnownRevisionRepository()
    }

    @Provides
    @AppScope
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    @Provides
    @AppScope
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .hostnameVerifier(CustomHostnameVerifier("beta.mrdekk.ru"))
            .build()
    }

    @Provides
    @AppScope
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todo/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @AppScope
    fun provideTodoApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @AppScope
    fun provideServerTodoItemMapper(deviceNameRepository: DeviceNameRepository): ServerTodoItemMapper {
        return ServerTodoItemMapper(deviceNameRepository)
    }*/


}