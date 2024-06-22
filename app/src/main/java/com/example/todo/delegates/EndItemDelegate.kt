package com.example.homework1.delegates

import android.annotation.SuppressLint
import android.view.MotionEvent
import com.example.homework1.TodoAdapter
import com.example.homework1.databinding.NewItemBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun endTextItemDelegate(onEndItemClick: () -> Unit) = adapterDelegateViewBinding<String, Any, NewItemBinding>(
    { inflater, parent -> NewItemBinding.inflate(inflater, parent, false) }
) {
    bind {
        binding.root.setOnClickListener { onEndItemClick() }
    }
}