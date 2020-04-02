package com.codingwithmitch.notes.datasource.cache.repository

import com.codingwithmitch.cleannotes.notes.datasource.mappers.NoteEntityMapper
import com.codingwithmitch.cleannotes.notes.data.datasource.NoteCacheDataSource
import com.codingwithmitch.cleannotes.notes.datasource.model.NoteEntity
import com.codingwithmitch.cleannotes.notes.domain.model.Note
import com.codingwithmitch.cleannotes.util.DateUtil
import com.codingwithmitch.notes.datasource.cache.db.NoteDao
import com.codingwithmitch.notes.di.FeatureScope
import javax.inject.Inject

@FeatureScope
class NoteCacheDataSourceImpl
@Inject
constructor(
    private val noteDao: NoteDao,
    private val noteEntityMapper: NoteEntityMapper,
    private val dateUtil: DateUtil
): NoteCacheDataSource {

    override suspend fun insertNewNote(title: String, body: String): Long {
        val note = NoteEntity(
            id = null,
            title = title,
            body = body,
            created_at = dateUtil.convertServerStringDateToLong(dateUtil.getCurrentTimestamp()),
            updated_at = dateUtil.convertServerStringDateToLong(dateUtil.getCurrentTimestamp())
        )
        return noteDao.insertNote(note)
    }

    override suspend fun deleteNote(primaryKey: Int): Int {
        return noteDao.deleteNote(primaryKey)
    }

    override suspend fun updateNote(note: Note, newTitle: String?, newBody: String?): Int {
        val newNote = NoteEntity(
            id = note.id,
            title = newTitle?: note.title,
            body = newBody?: note.body,
            created_at = dateUtil.convertServerStringDateToLong(note.created_at),
            updated_at = dateUtil.convertServerStringDateToLong(dateUtil.getCurrentTimestamp())
        )
        return noteDao.updateNote(newNote)
    }

    override suspend fun get(): List<Note> {
        return noteEntityMapper.entityListToNoteList(noteDao.getNotes())

    }


}






















