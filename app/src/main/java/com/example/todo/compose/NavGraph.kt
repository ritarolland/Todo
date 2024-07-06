package com.example.todo.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todo.TodoViewModel


@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel: TodoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreenContent(
                viewModel = viewModel,
                navigateToAdd = { id ->
                    if (id != null) {
                        navController.navigate("add/$id")
                    } else {
                        navController.navigate("add") {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable("add/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            AddScreen(id = id, navController=navController,viewModel = viewModel)
        }
        composable("add") {
            AddScreen(null,navController=navController, viewModel = viewModel)
        }
    }
}