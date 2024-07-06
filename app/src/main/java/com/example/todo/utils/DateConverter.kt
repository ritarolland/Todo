package com.example.todo.utils

import java.util.Date

object DateConverter {
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    fun timestampToDate(timestamp: Long): Date {
        return Date(timestamp)
    }
}