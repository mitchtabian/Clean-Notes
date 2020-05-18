package com.codingwithmitch.cleannotes.framework.datasource.cache.implementation

import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.domain.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.cache.abstraction.NoteDaoService
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.returnOrderedQuery
import com.codingwithmitch.cleannotes.framework.datasource.cache.mappers.CacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDaoServiceImpl
@Inject
constructor(
    private val noteDao: NoteDao,
    private val noteMapper: CacheMapper,
    private val dateUtil: DateUtil
): NoteDaoService {

    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(noteMapper.mapToEntity(note))
    }

    override suspend fun insertNotes(notes: List<Note>): LongArray {
        return noteDao.insertNotes(
            noteMapper.noteListToEntityList(notes)
        )
    }

    override suspend fun searchNoteById(id: String): Note? {
        return noteDao.searchNoteById(id)?.let { note ->
            noteMapper.mapFromEntity(note)
        }
    }

    override suspend fun updateNote(
        primaryKey: String,
        title: String,
        body: String?
    ): Int {
        return noteDao.updateNote(
            primaryKey = primaryKey,
            title = title,
            body = body,
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    override suspend fun deleteNote(primaryKey: String): Int {
        return noteDao.deleteNote(primaryKey)
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        val ids = notes.mapIndexed {index, value -> value.id}
        return noteDao.deleteNotes(ids)
    }

    override suspend fun searchNotes(): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotes()
        )
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteMapper.entityListToNoteList(noteDao.getAllNotes())
    }

    override suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotesOrderByDateDESC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotesOrderByDateASC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotesOrderByTitleDESC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.searchNotesOrderByTitleASC(
                query = query,
                page = page,
                pageSize = pageSize
            )
        )
    }

    override suspend fun getNumNotes(): Int {
        return noteDao.getNumNotes()
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return noteMapper.entityListToNoteList(
            noteDao.returnOrderedQuery(
                query = query,
                page = page,
                filterAndOrder = filterAndOrder
            )
        )
    }
}




