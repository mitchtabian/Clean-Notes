package com.codingwithmitch.cleannotes.notes.business.data.abstraction

import com.codingwithmitch.cleannotes.notes.business.domain.model.Note

interface NoteCacheDataSource{

    suspend fun insertNewNote(title: String, body: String): Long

    suspend fun restoreDeletedNote(
        title: String,
        body: String,
        created_at: String,
        updated_at: String
    ): Long

    suspend fun deleteNote(primaryKey: Int): Int

    suspend fun updateNote(primaryKey: Int, newTitle: String, newBody: String?): Int

    suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note>

    suspend fun getNumNotes(): Int

    suspend fun insertNotes(notes: List<Note>): LongArray
}






