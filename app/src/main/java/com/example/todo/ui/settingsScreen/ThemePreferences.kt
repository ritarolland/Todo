package com.example.todo.ui.settingsScreen

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemePreferences@Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val THEME_KEY = "theme_key"
    }

    fun getTheme(): ThemeOption {
        val themeName = sharedPreferences.getString(THEME_KEY, ThemeOption.System.name) ?: ThemeOption.System.name
        return ThemeOption.valueOf(themeName)
    }

    fun setTheme(theme: ThemeOption) {
        sharedPreferences.edit().putString(THEME_KEY, theme.name).apply()
    }
}