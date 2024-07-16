package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.navigation.compose.rememberNavController
import com.example.todo.app.App
import com.example.todo.di.activity.MainActivityComponent
import com.example.todo.di.activity.MainActivityModule
import com.example.todo.ui.composable.NavGraph
import com.example.todo.ui.mainScreen.MainScreenFragment
import com.example.todo.ui.theme.ToDoAppTheme
import dagger.hilt.android.AndroidEntryPoint

/*
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}*/

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    lateinit var mainActivityComponent: MainActivityComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*setContent {
            val navController = rememberNavController()
            NavGraph(navController = navController)
        }*/

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MainScreenFragment())
            }
        }
    }
}
