package com.example.todo.ui.addScreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todo.R
import com.example.todo.data.ListRepository
import com.example.todo.data.TodoItemsRepository
import com.example.todo.data.TodoItemsRepositoryImpl
import com.example.todo.domain.models.Importance
import com.example.todo.domain.models.TodoItem
import com.example.todo.domain.models.TodoItemDao
import com.example.todo.domain.models.TodoItemDao_Impl
import com.example.todo.ui.mainScreen.TodoViewModel
import com.example.todo.ui.settingsScreen.ThemeOption
import com.example.todo.ui.theme.ToDoAppTheme
import com.example.todo.utils.DateFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    viewModel: AddScreenViewModel = hiltViewModel(),
) {
    val todoItem by viewModel.todoItem.collectAsState()
    val errorMessage by viewModel.errorFlow.collectAsState()
    val context = LocalContext.current
    var switchState by remember { mutableStateOf(todoItem.deadline != null) }
    val importanceItems = stringArrayResource(R.array.importance_options)

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = stringResource(id = R.string.button_back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveItem()
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.save),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 16.sp
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentAlignment = Alignment.TopStart
            ) {
                TextField(
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = todoItem.text,
                    onValueChange = { viewModel.updateText(it) },
                    placeholder = { Text(stringResource(id = R.string.what_to_do)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }

            Text(
                text = stringResource(id = R.string.importance),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = getStringFromImportance(todoItem.importance, importanceItems),
                            color = Color.Black,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                ) {

                    importanceItems.forEach { importanceText ->
                        CreateDropdownMenuItem(
                            importanceText,
                            updateImportance = viewModel::updateImportance
                        ) { expanded = it }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Column(

                    modifier = Modifier
                        .widthIn()
                        .heightIn()
                ) {
                    Text(
                        text = stringResource(id = R.string.until),
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (todoItem.deadline == null) {
                            stringResource(id = R.string.date_and_time)
                        } else {
                            todoItem.deadline?.let { DateFormatter.toFormat(it) } ?: ""
                        },
                        fontSize = 16.sp,
                        color = Color.Gray.copy(alpha = 1f)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = switchState,
                    onCheckedChange = {
                        if (switchState) {
                            switchState = false
                            viewModel.updateDeadline(null)
                        } else {
                            switchState = true
                            onShowDialog(
                                context = context,
                                onDismissRequest = {
                                    switchState = false
                                },
                                updateDeadline = viewModel::updateDeadline
                            )
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                        uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(thickness = 1.dp, color = Color.Gray)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable(onClick = {
                        if (todoItem.id.isNotEmpty()) {
                            viewModel.deleteItem()
                            navController.popBackStack()
                        }
                    })
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_red),
                    contentDescription = "Delete icon",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.delete),
                    color = Color.Red,
                    fontSize = 16.sp
                )
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

private fun onShowDialog(
    context: Context,
    onDismissRequest: () -> Unit,
    updateDeadline: (Date) -> Unit,
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            updateDeadline(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.setOnCancelListener {
        onDismissRequest()
    }
    datePickerDialog.show()
}

@Composable
fun CreateDropdownMenuItem(
    importanceText: String,
    updateImportance: (Importance) -> Unit,
    onExpandedChanged: (Boolean) -> Unit
) {
    val importanceItems = stringArrayResource(R.array.importance_options)
    DropdownMenuItem(
        text = { Text(importanceText, color = MaterialTheme.colorScheme.onPrimary) },
        onClick = {
            val importance = getImportanceFromText(importanceText, importanceItems)
            updateImportance(importance)
            onExpandedChanged(false)
        },
    )
}

fun getImportanceFromText(importanceText: String, importanceItems: Array<String>): Importance {
    return when (importanceText) {
        importanceItems[0] -> Importance.NORMAL
        importanceItems[1] -> Importance.HIGH
        importanceItems[2] -> Importance.LOW
        else -> Importance.NORMAL
    }
}

fun getStringFromImportance(importance: Importance, importanceItems: Array<String>): String {
    return when (importance) {
        Importance.LOW -> importanceItems[2]
        Importance.NORMAL -> importanceItems[0]
        Importance.HIGH -> importanceItems[1]
    }
}

class MockTodoItemsRepository : TodoItemsRepository {
    private val mockTodoItem = TodoItem(
        id = "1",
        text = "Sample task",
        importance = Importance.NORMAL,
        deadline = null,
        isDone = false,
        createdAt = Date(),
        updatedAt = Date()
    )

    override suspend fun updateChecked(id: String, isDone: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getToDoById(id: String): TodoItem {
        TODO("Not yet implemented")
    }

    override suspend fun addOrEditToDo(item: TodoItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteToDo(id: String) {
        TODO("Not yet implemented")
    }
}
/*

@Preview(showBackground = true)
@Composable
fun PreviewAddScreen() {
    val mockTodoItem = TodoItem(
        id = "1",
        text = "Sample task",
        importance = Importance.NORMAL,
        deadline = null,
        isDone = false,
        createdAt = Date(),
        updatedAt = Date()
    )
    val mockRepository = MockTodoItemsRepository()
    val mockViewModel = AddScreenViewModel(mockRepository)
    val navController = rememberNavController()
    ToDoAppTheme(themeOption = ThemeOption.Light) {
        AddScreen(viewModel = mockViewModel, navController = navController)
    }
}*/
