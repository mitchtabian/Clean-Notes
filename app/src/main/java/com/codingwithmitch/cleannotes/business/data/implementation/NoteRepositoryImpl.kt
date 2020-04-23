package com.codingwithmitch.cleannotes.business.data.implementation

import com.codingwithmitch.cleannotes.business.data.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.business.domain.model.Note
import javax.inject.Inject

class NoteRepositoryImpl
@Inject
constructor(
    private val noteCacheDataSource: NoteCacheDataSource
): NoteRepository {

    override suspend fun insertNote(note: Note): Long {
        return noteCacheDataSource.insertNote(note)
    }

    override suspend fun deleteNote(primaryKey: String): Int {
        return noteCacheDataSource.deleteNote(primaryKey)
    }

    override suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String?): Int {
        return noteCacheDataSource.updateNote(primaryKey, newTitle, newBody)
    }

    override suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return noteCacheDataSource.searchNotes(
            query = query,
            filterAndOrder = filterAndOrder,
            page = page
        )
    }

    override suspend fun getNumNotes() = noteCacheDataSource.getNumNotes()

    override suspend fun insertNotes(notes: List<Note>) = noteCacheDataSource.insertNotes(notes)
}
















