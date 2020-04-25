package com.codingwithmitch.cleannotes.business.data.cache.abstraction

import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot


interface NoteCacheDataSource{

    suspend fun insertNote(note: Note): Long

    suspend fun deleteNote(primaryKey: String): Int

    suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String?): Int

    suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note>

    suspend fun searchNoteById(id: String): Note?

    suspend fun getNumNotes(): Int

    suspend fun insertNotes(notes: List<Note>): LongArray
}






