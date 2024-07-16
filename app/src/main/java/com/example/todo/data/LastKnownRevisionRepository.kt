package com.example.todo.data

import com.example.todo.di.app.AppScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastKnownRevisionRepository @Inject constructor()
{
    var lastKnownRevision: Int? = 0
        private set

    fun updateRevision(revision: Int) {
        lastKnownRevision = revision
    }
}