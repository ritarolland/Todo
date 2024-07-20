package com.example.todo.ui.settingsScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreferences: ThemePreferences
) : ViewModel() {
    private val _selectedTheme = MutableStateFlow(themePreferences.getTheme())
    val selectedTheme: StateFlow<ThemeOption> get() = _selectedTheme

    fun setTheme(theme: ThemeOption) {
        themePreferences.setTheme(theme)
        _selectedTheme.value = theme
    }
}