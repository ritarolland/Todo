package com.example.todo.delegates

import android.annotation.SuppressLint
import android.view.MotionEvent
import com.example.todo.TodoAdapter
import com.example.todo.databinding.NewItemBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun endTextItemDelegate(onEndItemClick: () -> Unit) = adapterDelegateViewBinding<String, Any, NewItemBinding>(
    { inflater, parent -> NewItemBinding.inflate(inflater, parent, false) }
) {
    bind {
        binding.root.setOnClickListener { onEndItemClick() }
    }
}