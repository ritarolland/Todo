package com.example.todo.ui.composable

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todo.ui.aboutScreen.AboutScreen
import com.example.todo.ui.mainScreen.TodoViewModel
import com.example.todo.ui.addScreen.AddScreen
import com.example.todo.ui.mainScreen.MainScreenContent
import com.example.todo.ui.addScreen.AddScreenViewModel
import com.example.todo.ui.theme.ToDoAppTheme


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            ToDoAppTheme {
                MainScreenContent(
                    navigateToAdd = { id ->
                        if (id != null) {
                            navController.navigate("add/$id")
                        } else {
                            navController.navigate("add") {
                                launchSingleTop = true
                            }
                        }
                    },
                    navigateAbout = {
                        navController.navigate("about")
                    }
                )
            }

        }
        composable("about") {
            AboutScreen(navController = navController)
        }
        composable("add/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            AddScreen(navController = navController)////////////////////
        }
        composable("add") {
            AddScreen(navController = navController)//////////////////
        }
    }
}