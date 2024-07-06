package com.example.todo.compose

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.TodoViewModel
import com.example.todo.ui.ToDoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    viewModel: TodoViewModel,
    navigateToAdd: (String?) -> Unit
) {
    val tasks by viewModel.todos.collectAsState()
    val completedTasksCount by viewModel.completedTasksCount.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val showCompletedTasks by viewModel.showCompletedTasks.collectAsState()
    val context = LocalContext.current

    val errorState = viewModel.errorFlow
    LaunchedEffect(viewModel) {
        viewModel.updateTodos()
        errorState.collect {
            if (it != null)
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }


    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = if (scrollBehavior.state.collapsedFraction > 0.5) 6.dp else 0.dp,
                        shape = RoundedCornerShape(0.dp))
            ){
                LargeTopAppBar(
                    modifier = Modifier
                        .animateContentSize()
                        .background(MaterialTheme.colorScheme.surface),
                    title = {
                        Text(
                            text = "Мои дела",
                            color = MaterialTheme.colorScheme.onPrimary

                        ) },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        Text(text = "Выполнено - $completedTasksCount",
                            color = MaterialTheme.colorScheme.onTertiary)
                        IconButton(onClick = {
                            viewModel.toggleShowCompletedTasks()
                        }) {
                            Icon(
                                painter = painterResource(
                                    id = if (showCompletedTasks) R.drawable.visibility else R.drawable.visibility_off
                                ),
                                contentDescription = "Toggle Visibility",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }


                )
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAdd(null) },
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceTint,
                            shape = RoundedCornerShape(8.dp)),
                    contentPadding = paddingValues
                ) {
                    items(tasks) { task ->
                        //var isChecked by remember { mutableStateOf(task.isDone) }
                        Todo(
                            item = task,
                            onComplete = {
                                viewModel.addOrEditTodoItem(task.copy(isDone = !task.isDone))
                                task.isDone = it

                                viewModel.addOrEditTodoItem(task)

                            },
                            onCardClick = {
                                navigateToAdd(task.id)
                            }
                        )

                    }
                    item {
                        Text(
                            text = "Новое",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .padding(horizontal = 52.dp, vertical = 8.dp)

                        )
                    }
                }
        }

    }
}


@Preview
@Composable
fun PreviewMainScreen() {
    val viewModel = TodoViewModel()
    ToDoAppTheme(darkTheme = false, dynamicColor = false) {
        MainScreenContent(viewModel = viewModel, navigateToAdd = {})
    }
}

@Preview
@Composable
fun PreviewMainScreenDark() {
    val viewModel = TodoViewModel()
    ToDoAppTheme(darkTheme = true, dynamicColor = false) {
        MainScreenContent(viewModel = viewModel, navigateToAdd = {})
    }
}
