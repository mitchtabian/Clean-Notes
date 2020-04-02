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

    override suspend fun insert(note: Note): Long {
        return noteCacheDataSource.insert(note)
    }

    override suspend fun delete(note: Note): Int {
        return noteCacheDataSource.delete(note)
    }

    override suspend fun update(note: Note): Int {
        return noteCacheDataSource.update(note)
    }

    override suspend fun get(): List<Note> {
        return noteCacheDataSource.get()
    }


}
















