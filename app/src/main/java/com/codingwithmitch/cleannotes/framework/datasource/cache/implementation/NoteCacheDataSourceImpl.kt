package com.codingwithmitch.cleannotes.framework.datasource.cache.implementation

import com.codingwithmitch.cleannotes.business.data.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.cache.abstraction.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.abstraction.returnOrderedQuery
import com.codingwithmitch.cleannotes.framework.datasource.mappers.NoteMapper
import com.codingwithmitch.cleannotes.framework.datasource.model.NoteEntity
import javax.inject.Inject

class NoteCacheDataSourceImpl
@Inject
constructor(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper,
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

    override suspend fun restoreDeletedNote(
        title: String,
        body: String,
        created_at: String,
        updated_at: String
    ): Long {
        return noteDao.restoreDeletedNote(
            title = title,
            body = body,
            created_at = dateUtil.convertServerStringDateToLong(created_at),
            updated_at = dateUtil.convertServerStringDateToLong(updated_at)
        )
    }

    override suspend fun deleteNote(primaryKey: Int): Int {
        return noteDao.deleteNote(primaryKey)
    }

    override suspend fun updateNote(primaryKey: Int, newTitle: String, newBody: String?): Int {
        return noteDao.updateNote(
            primaryKey = primaryKey,
            title = newTitle,
            body = newBody,
            updated_at = dateUtil.convertServerStringDateToLong(dateUtil.getCurrentTimestamp())
        )
    }

    override suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.returnOrderedQuery(
                query = query,
                filterAndOrder = filterAndOrder,
                page = page
            )
        )
    }

    override suspend fun getNumNotes() = noteDao.getNumNotes()

    override suspend fun insertNotes(notes: List<Note>): LongArray{
        return noteDao.insertNotes(
            noteMapper.noteListToEntityList(notes)
        )
    }
}






















