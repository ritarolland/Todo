package com.example.todo.utils

import com.example.todo.domain.models.Importance

object ImportanceConverter {
    fun stringToImportance(string: String): Importance{
        return when (string){
            "low" -> Importance.LOW
            "basic" -> Importance.NORMAL
            "important" -> Importance.HIGH
            else -> Importance.NORMAL
        }
    }

    fun importanceToString(importance: Importance): String{
        return when(importance){
            Importance.LOW -> "low"
            Importance.NORMAL -> "basic"
            Importance.HIGH -> "important"
        }
    }
}