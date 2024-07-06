package com.example.todo.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastKnownRevisionRepository @Inject constructor()
{
    var lastKnownRevision: Int? = 5
        private set

    fun updateRevision(revision: Int) {
        lastKnownRevision = revision
    }
}