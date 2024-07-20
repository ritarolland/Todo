package com.example.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todo.ui.aboutScreen.NavigationDivActionHandler
import com.example.todo.ui.composable.NavGraph
import com.example.todo.ui.settingsScreen.SettingsViewModel
import com.example.todo.ui.settingsScreen.ThemeOption
import com.example.todo.ui.settingsScreen.ThemePreferences
import com.example.todo.ui.theme.LocalThemeOption
import com.example.todo.ui.theme.ToDoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val selectedTheme by settingsViewModel.selectedTheme.collectAsState()
            CompositionLocalProvider(LocalThemeOption provides selectedTheme) {
                ToDoAppTheme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        val navController: NavHostController = rememberNavController()
                        NavGraph(navController = navController, settingsViewModel)
                    }
                }
            }
            /*ToDoAppTheme() {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }*/

        }
    }
}
