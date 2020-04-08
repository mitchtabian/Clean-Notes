package com.codingwithmitch.cleannotes.notes.business.data.datasource

import com.codingwithmitch.cleannotes.notes.business.domain.model.Note

interface NoteCacheDataSource{

    suspend fun insertNewNote(title: String, body: String): Long

    suspend fun deleteNote(primaryKey: Int): Int

    suspend fun updateNote(primaryKey: Int, newTitle: String, newBody: String?): Int

    suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note>

    suspend fun getNumNotes(): Int
}

interface NoteNetworkDataSource{

    suspend fun insert(note: Note): Long

    suspend fun delete(note: Note): Int

    suspend fun update(note: Note): Int

    suspend fun get(): List<Note>

}





