package com.codingwithmitch.notes.data.repository

import com.codingwithmitch.notes.data.datasource.NoteCacheDataSource
import com.codingwithmitch.notes.di.NoteScope
import com.codingwithmitch.notes.domain.model.Note
import com.codingwithmitch.notes.domain.repository.NoteRepository
import javax.inject.Inject

@NoteScope
class NoteRepositoryImpl
@Inject
constructor(
    private val noteCacheDataSource: NoteCacheDataSource
): NoteRepository{

    override suspend fun insert(note: Note)
            = noteCacheDataSource.insert(note)

    override suspend fun delete(note: Note)
            = noteCacheDataSource.delete(note)

    override suspend fun update(note: Note)
            = noteCacheDataSource.update(note)

    override suspend fun get(): List<Note>
            = noteCacheDataSource.get()

}













