package com.codingwithmitch.cleannotes.notes.data.repository

import com.codingwithmitch.cleannotes.notes.data.datasource.NoteCacheDataSource
import com.codingwithmitch.cleannotes.notes.domain.model.Note
import com.codingwithmitch.cleannotes.notes.domain.repository.NoteRepository
import com.codingwithmitch.notes.di.FeatureScope
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

    override suspend fun deleteNote(primaryKey: Int): Int {
        return noteCacheDataSource.deleteNote(primaryKey)
    }

    override suspend fun updateNote(note: Note, newTitle: String?, newBody: String?): Int {
        return noteCacheDataSource.updateNote(note, newTitle, newBody)
    }

    override suspend fun get(): List<Note> {
        return noteCacheDataSource.get()
    }


}
















