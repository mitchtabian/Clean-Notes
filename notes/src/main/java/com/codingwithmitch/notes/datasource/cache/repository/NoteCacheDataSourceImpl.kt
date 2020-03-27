package com.codingwithmitch.notes.datasource.cache.repository

import com.codingwithmitch.notes.data.datasource.NoteCacheDataSource
import com.codingwithmitch.notes.datasource.cache.db.NoteDao
import com.codingwithmitch.notes.domain.model.Note
import com.codingwithmitch.notes.datasource.mappers.NoteEntityMapper
import com.codingwithmitch.notes.di.NoteScope
import javax.inject.Inject
import javax.inject.Singleton

@NoteScope
class NoteCacheDataSourceImpl
@Inject
constructor(
    private val noteDao: NoteDao,
    private val noteEntityMapper: NoteEntityMapper
):NoteCacheDataSource{

    override suspend fun insert(note: Note): Long {
        return noteDao.insertNote(noteEntityMapper.noteToEntity(note))
    }

    override suspend fun delete(note: Note): Int {
        return noteDao.deleteNote(noteEntityMapper.noteToEntity(note))
    }

    override suspend fun update(note: Note): Int {
        return noteDao.updateNote(noteEntityMapper.noteToEntity(note))
    }

    override suspend fun get(): List<Note> {
        return noteEntityMapper.entityListToNoteList(noteDao.getNotes())

    }


}






















