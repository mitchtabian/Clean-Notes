package com.codingwithmitch.cleannotes.framework.datasource.cache.implementation

import com.codingwithmitch.cleannotes.framework.datasource.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.returnOrderedQuery
import com.codingwithmitch.cleannotes.framework.datasource.cache.mappers.CacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCacheDataSourceImpl
@Inject
constructor(
    private val noteDao: NoteDao,
    private val noteMapper: CacheMapper,
    private val dateUtil: DateUtil
): NoteCacheDataSource {

    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(noteMapper.mapToEntity(note))
    }

    override suspend fun deleteNote(primaryKey: String): Int {
        return noteDao.deleteNote(primaryKey)
    }

    override suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String?): Int {
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






















