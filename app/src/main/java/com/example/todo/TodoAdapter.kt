package com.example.homework1

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homework1.databinding.TodoItemBinding
import com.example.homework1.delegates.endTextItemDelegate
import com.example.homework1.delegates.normalItemDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class TodoAdapter(
    private var todos: MutableList<Any>,
    onTaskClick: (TodoItem) -> Unit,
    onEndItemClick: () -> Unit,
    private val updateCompletedTasksCount: () -> Unit
) : ListDelegationAdapter<List<Any>>() {

    private var showCompletedTasks: Boolean = true

    init {
        delegatesManager.addDelegate(normalItemDelegate(onTaskClick, updateCompletedTasksCount))
        delegatesManager.addDelegate(endTextItemDelegate(onEndItemClick))
        setItems(todos)
    }

    fun deleteItem(position: Int) {
        todos = todos.toMutableList().apply {
            removeAt(position)
        }
        notifyItemRemoved(position)
        updateCompletedTasksCount()
    }

    fun checkItem(position: Int) {
        (items?.get(position) as TodoItem).isDone = true
        notifyItemChanged(position)
        updateCompletedTasksCount()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterTodos() {
        items = if (showCompletedTasks) {
            todos
        } else {
            todos.filter { it !is TodoItem || !it.isDone }.toMutableList()
        }

        notifyDataSetChanged()
    }

    fun toggleShowCompletedTasks() {
        showCompletedTasks = !showCompletedTasks
        filterTodos()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addTask(task: TodoItem) {
        items = items?.toMutableList()?.apply {
            add(size - 1, task) // Добавляем новую задачу перед последним элементом
        }
        notifyItemInserted(items!!.size - 2)
    }

    fun countCompletedTasks(): Int {
        return todos.count { it is TodoItem && it.isDone }
    }
}
