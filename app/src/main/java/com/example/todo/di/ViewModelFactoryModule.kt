package com.example.todo.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

/*
@Module
@InstallIn(ActivityComponent::class)
object ViewModelFactoryModule {

    @Provides
    @ActivityScoped
    fun provideViewModelFactory(activity: FragmentActivity): ViewModelProvider.Factory {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application)
    }
}*/
