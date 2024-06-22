package com.example.todo.delegates
import android.annotation.SuppressLint
import android.graphics.Paint
import android.opengl.Visibility
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginLeft
import com.example.todo.Importance
import com.example.todo.R
import com.example.todo.TodoItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.example.todo.databinding.TodoItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UseCompatLoadingForDrawables")
fun normalItemDelegate(onTaskClick: (TodoItem) -> Unit,
                       updateCompletedTasksCount: () -> Unit) =
    adapterDelegateViewBinding<TodoItem, Any, TodoItemBinding>(
        { layoutInflater, root -> TodoItemBinding.inflate(layoutInflater, root, false) }
    ) {
        binding.checkbox.setOnCheckedChangeListener(null)

        bind {
            if(item.deadline!= null) {
                binding.date.text =
                    item.deadline?.let { it1 ->
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                            it1
                        )
                    }
            } else binding.date.visibility = View.INVISIBLE
            binding.todoText.text = item.text
            binding.checkbox.isChecked = item.isDone
            updateTextStrikethrough(binding.todoText, item.isDone)


            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                item.isDone = isChecked
                updateTextStrikethrough(binding.todoText, isChecked)
                if (!isChecked && item.importance != Importance.NORMAL) {
                    binding.importance.visibility = View.VISIBLE
                } else {
                    binding.importance.visibility = View.GONE
                }
                updateCompletedTasksCount()
            }

            if (item.importance == Importance.HIGH) {
                binding.importance.setImageResource(R.drawable.important)
                binding.checkbox.setCompoundDrawablesWithIntrinsicBounds(
                    context.getDrawable(R.drawable.high_checkbox), null, null, null
                )
            } else if (item.importance == Importance.LOW) {
                binding.importance.setImageResource(R.drawable.not_important)
                binding.checkbox.setCompoundDrawablesWithIntrinsicBounds(
                    context.getDrawable(R.drawable.checkbox), null, null, null
                )
            } else {
                binding.checkbox.setCompoundDrawablesWithIntrinsicBounds(
                    context.getDrawable(R.drawable.checkbox), null, null, null
                )
            }

            // Update the visibility of the importance icon based on the checkbox state
            if (!binding.checkbox.isChecked && item.importance != Importance.NORMAL) {
                binding.importance.visibility = View.VISIBLE
            } else {
                binding.importance.visibility = View.GONE
            }

            binding.info.setOnClickListener { onTaskClick(item) }


        }
    }

private fun updateTextStrikethrough(textView: TextView, isChecked: Boolean) {
    if (isChecked) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}