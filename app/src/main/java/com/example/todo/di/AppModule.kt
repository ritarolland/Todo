package com.example.todo.di

import android.content.Context
import android.content.SharedPreferences
import com.example.todo.ui.settingsScreen.ThemePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideThemePreferences(sharedPreferences: SharedPreferences): ThemePreferences {
        return ThemePreferences(sharedPreferences)
    }
}