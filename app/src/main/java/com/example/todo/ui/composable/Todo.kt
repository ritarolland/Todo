package com.example.todo.ui.composable

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.domain.models.Importance

import com.example.todo.domain.models.TodoItem
import com.example.todo.ui.theme.ToDoAppTheme

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Todo(
    item: TodoItem,
    onComplete: (String, Boolean) -> Unit,
    onCardClick: () -> Unit,
) {


    val checkBoxColor = if (item.importance == Importance.HIGH) {
        CheckboxDefaults.colors().copy(
            checkedBorderColor = MaterialTheme.colorScheme.scrim,
            checkedBoxColor = MaterialTheme.colorScheme.scrim,
            uncheckedBoxColor = MaterialTheme.colorScheme.error.copy(alpha = 0.16f),
            uncheckedBorderColor = MaterialTheme.colorScheme.error,
            checkedCheckmarkColor = MaterialTheme.colorScheme.primaryContainer
        )
    } else {
        CheckboxDefaults.colors().copy(
            checkedBorderColor = MaterialTheme.colorScheme.scrim,
            checkedBoxColor = MaterialTheme.colorScheme.scrim
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onCardClick() }
            ),
        shape = RectangleShape,
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.surfaceTint)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Checkbox(
                checked = item.isDone,
                onCheckedChange = {
                    onComplete(item.id, it)
                },
                colors = checkBoxColor
            )

            ImportanceIcon(importance = item.importance, modifier = Modifier.padding(end = 4.dp, top = 14.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                TodoItemText(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, start = 4.dp, end = 8.dp),
                    item = item)
                /*Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp, start = 4.dp, end = 8.dp),
                    text = item.text,
                    fontSize = 16.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = if (item.isDone) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle()
                )*/

                if (item.deadline != null) {
                    Text(
                        text = SimpleDateFormat(
                            "dd.MM.yyyy",
                            Locale.getDefault()
                        ).format(item.deadline),
                        Modifier.padding(bottom = 14.dp, top = 8.dp, start = 4.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Icon(
                Icons.Outlined.Info,
                null,
                tint = Color.Gray,
                modifier = Modifier.padding(start = 4.dp, top = 14.dp, end = 14.dp)
            )

        }
    }
}

@Composable
fun TodoItemText(item: TodoItem, modifier: Modifier) {
    val baseStyle = MaterialTheme.typography.labelMedium

    val textStyle = if (item.isDone) {
        baseStyle.copy(textDecoration = TextDecoration.LineThrough)
    } else {
        baseStyle
    }

    Text(
        modifier = modifier,
        text = item.text,
        style = textStyle,
        color = MaterialTheme.colorScheme.onPrimary,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Preview
@Composable
fun ToDoItemBasic() {
    ToDoAppTheme(darkTheme = false, dynamicColor = false) {
        Todo(
            TodoItem(
                id = "1",
                text = "It is first note",
                importance = Importance.NORMAL,
                isDone = false,
                deadline = null,
                createdAt = Date(),
                updatedAt = null
            ),
            {_, _ -> },
            {},
        )
    }
}

@Preview
@Composable
fun ToDoItemHighPriority() {
    ToDoAppTheme(darkTheme = false, dynamicColor = false) {
        Todo(
            TodoItem(
                id = "2",
                text = "It is second note",
                importance = Importance.HIGH,
                isDone = true,
                deadline = Date(1734775200000),
                createdAt = Date(),
                updatedAt = null
            ),
            {_, _ ->},
            {},
        )
    }
}

@Preview
@Composable
fun ToDoItemWithDeadline() {
    ToDoAppTheme(darkTheme = false, dynamicColor = false) {
        Todo(
            TodoItem(
                id = "1",
                text = "It is first note",
                importance = Importance.NORMAL,
                isDone = false,
                deadline = Date(),
                createdAt = Date(),
                updatedAt = null
            ),
            {_, _ ->},
            {},
        )
    }
}


@Preview
@Composable
fun ToDoItemLowPriority() {
    ToDoAppTheme(darkTheme = true, dynamicColor = false) {
        Todo(
            TodoItem(
                id = "3",
                text = "It is third note",
                importance = Importance.LOW,
                isDone = true,
                deadline = Date(1734774200000),
                createdAt = Date(),
                updatedAt = Date(1734774190000)
            ),
            {_, _ ->},
            {},
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ToDoItemLotText() {
    ToDoAppTheme(darkTheme = false, dynamicColor = false) {
        Todo(
            TodoItem(
                id = "4",
                text = "A lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot lot text",
                importance = Importance.HIGH,
                isDone = false,
                deadline = Date(1734734200000),
                createdAt = Date(),
                updatedAt = Date(1734774190000)
            ),
            {_, _ ->},
            {},
        )
    }
}
