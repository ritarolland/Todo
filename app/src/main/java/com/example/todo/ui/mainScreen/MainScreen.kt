package com.example.todo.ui.mainScreen

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.R
import com.example.todo.ui.theme.ToDoAppTheme
import com.example.todo.ui.composable.Todo
import com.example.todo.ui.addScreen.AddScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    viewModel: TodoViewModel = hiltViewModel(),
    addScreenViewModel: AddScreenViewModel = hiltViewModel(),
    navigateToAdd: (String?) -> Unit
) {
    val mainScreenUiModel by viewModel.mainScreenUiModel.collectAsState()
    val errorMessage by viewModel.errorFlow.collectAsState()
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TodoTopAppBar(
                viewModel = viewModel,
                mainScreenUiModel = mainScreenUiModel,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addScreenViewModel.getItemById(null)
                    navigateToAdd(null)
                          },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceTint,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentPadding = paddingValues
            ) {
                items(mainScreenUiModel.tasks) { task ->
                    Todo(
                        item = task,
                        onComplete = viewModel::updateChecked,
                        onCardClick = {
                            addScreenViewModel.getItemById(task.id)
                            navigateToAdd(task.id)
                        }
                    )

                }
                item {
                    Text(
                        text = stringResource(id = R.string.newTodo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(horizontal = 52.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
    errorMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        LaunchedEffect(key1 = message) {
            viewModel.clearError()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopAppBar(
    viewModel: TodoViewModel,
    mainScreenUiModel: MainScreenUiModel,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        modifier = Modifier
            .animateContentSize()
            .background(MaterialTheme.colorScheme.surface),
        title = {
            Text(
                text = stringResource(id = R.string.my_todos),
                color = MaterialTheme.colorScheme.onPrimary

            )
        },
        scrollBehavior = scrollBehavior,
        actions = {
            Text(
                text = stringResource(id = R.string.done) + mainScreenUiModel.completedTasksCount,
                color = MaterialTheme.colorScheme.onTertiary
            )
            IconButton(onClick = {
                viewModel.toggleShowCompletedTasks()
            }) {
                Icon(
                    painter = painterResource(
                        id = if (mainScreenUiModel.showCompletedTasks) R.drawable.visibility
                        else R.drawable.visibility_off
                    ),
                    contentDescription = "Toggle Visibility",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    )
}


@Composable
@Preview(showBackground = true)
fun PreviewMainScreen() {
    ToDoAppTheme(darkTheme = false, dynamicColor = false) {
        MainScreenContent(navigateToAdd = {})
    }
}

//@Preview
//@Composable
//fun PreviewMainScreenDark() {
//    val viewModel = TodoViewModel()
//    ToDoAppTheme(darkTheme = true, dynamicColor = false) {
//        MainScreenContent(viewModel = viewModel, navigateToAdd = {})
//    }
//}
