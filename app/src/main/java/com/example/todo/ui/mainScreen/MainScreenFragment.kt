/*
package com.example.todo.ui.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todo.ui.composable.NavGraph
import com.example.todo.ui.theme.ToDoAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainScreenFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val navController = rememberNavController()
                NavGraph(navController = navController)
                */
/*ToDoAppTheme {

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
                    )
                }*//*

            }
        }
    }
}*/
