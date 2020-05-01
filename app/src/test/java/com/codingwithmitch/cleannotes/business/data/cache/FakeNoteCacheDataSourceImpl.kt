package com.codingwithmitch.cleannotes.business.data.cache

import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NOTE_PAGINATION_PAGE_SIZE
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val FORCE_DELETE_NOTE_EXCEPTION = "FORCE_EXCEPTION"

class FakeNoteCacheDataSourceImpl
constructor(
    private val notesData: HashMap<String, Note>
): NoteCacheDataSource{

    private val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    private val dateUtil = DateUtil(sdf)

    override suspend fun insertNote(note: Note): Long {
        notesData.put(note.id, note)
        return 1 // always successful
    }

    override suspend fun deleteNote(primaryKey: String): Int {
        if(primaryKey.equals(FORCE_DELETE_NOTE_EXCEPTION)){
            throw Exception("Something went wrong deleting the note.")
        }
        return notesData.remove(primaryKey)?.let { note ->
            1 // return 1 for success
        }?: - 1 // -1 for failure
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
        var failOrSuccess = 1
        for(note in notes){
            if(notesData.remove(note.id) == null){
                failOrSuccess = -1 // mark for failure
            }
        }
        return failOrSuccess
    }

    override suspend fun updateNote(
        primaryKey: String,
        newTitle: String,
        newBody: String?
    ): Int {
        val updatedNote = Note(
            id = primaryKey,
            title = newTitle,
            body = newBody?: "",
            updated_at = dateUtil.getCurrentTimestamp(),
            created_at = notesData.get(primaryKey)?.created_at?: dateUtil.getCurrentTimestamp()
        )
        return notesData.get(primaryKey)?.let { note ->
            notesData.put(primaryKey, updatedNote)
            1 // success
        }?: -1 // nothing to update
    }

    // Not testing the order/filter. Just basic query
    // simulate SQLite "LIKE" query on title and body
    override suspend fun searchNotes(
        query: String,
        filterAndOrder:
        String,
        page: Int
    ): List<Note> {
        val results: ArrayList<Note> = ArrayList()
        for(note in notesData.values){
            if(note.title.contains(query)){
                results.add(note)
            }
            if(note.body.contains(query)){
                results.add(note)
            }
            if(results.size > (page * NOTE_PAGINATION_PAGE_SIZE)){
                break
            }
        }
        return results
    }

    override suspend fun searchNoteById(id: String): Note? {
        return notesData.get(id)
    }

    override suspend fun getNumNotes(): Int {
        return notesData.size
    }

    override suspend fun insertNotes(notes: List<Note>): LongArray {
        val results = LongArray(notes.size)
        for((index,note) in notes.withIndex()){
            results[index] = 1
            notesData.put(note.id, note)
        }
        return results
    }
}
















