package com.example.todo.di.app

import android.content.Context
import com.example.todo.MainActivity
import com.example.todo.di.DataModule
import com.example.todo.di.NavigationModule
import com.example.todo.di.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class, DataModule::class, NavigationModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}