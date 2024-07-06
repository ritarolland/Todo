package com.example.todo.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private const val DATE_PATTERN = "d/MM/yyyy"
    @SuppressLint("ConstantLocale")
    private val formatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    fun toFormat(date: Date): String? =
        formatter.format(date)
}