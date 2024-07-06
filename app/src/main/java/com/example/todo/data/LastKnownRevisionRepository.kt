package com.example.todo.data

class LastKnownRevisionRepository {

    var lastKnownRevision: Int? = 5
        private set

    fun updateRevision(revision: Int) {
        lastKnownRevision = revision
    }
}