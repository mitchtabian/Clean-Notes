package com.codingwithmitch.cleannotes.notes.business.data.implementation

import com.codingwithmitch.cleannotes.notes.business.data.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import javax.inject.Inject

@FeatureScope
class NoteRepositoryImpl
@Inject
constructor(
    private val noteCacheDataSource: NoteCacheDataSource
): NoteRepository{

    override suspend fun insertNewNote(title: String, body: String): Long {
        return noteCacheDataSource.insertNewNote(title, body)
    }

    override suspend fun restoreDeletedNote(
        title: String,
        body: String,
        created_at: String,
        updated_at: String
    ): Long {
        return noteCacheDataSource.restoreDeletedNote(
            title, body, created_at, updated_at
        )
    }

    override suspend fun deleteNote(primaryKey: Int): Int {
        return noteCacheDataSource.deleteNote(primaryKey)
    }

    override suspend fun updateNote(primaryKey: Int, newTitle: String, newBody: String?): Int {
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
















