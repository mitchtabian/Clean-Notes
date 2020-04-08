package com.codingwithmitch.notes.datasource.cache.db

import androidx.room.*
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NOTE_PAGINATION_PAGE_SIZE
import com.codingwithmitch.cleannotes.notes.framework.datasource.model.NoteEntity

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    @Query("""
        UPDATE notes 
        SET 
        title = :title, 
        body = :body,
        updated_at = :updated_at
        WHERE id = :primaryKey
        """)
    suspend fun updateNote(
        primaryKey: Int,
        title: String,
        body: String?,
        updated_at: Long
    ): Int

    @Query("DELETE FROM notes WHERE id = :primaryKey")
    suspend fun deleteNote(primaryKey: Int): Int

    @Query("SELECT * FROM notes")
    suspend fun searchNotes(): List<NoteEntity>

    @Query("""
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY updated_at DESC LIMIT (:page * :pageSize)
        """)
    suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<NoteEntity>

    @Query("""
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY updated_at ASC LIMIT (:page * :pageSize)
        """)
    suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<NoteEntity>

    @Query("""
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY title DESC LIMIT (:page * :pageSize)
        """)
    suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<NoteEntity>

    @Query("""
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY title ASC LIMIT (:page * :pageSize)
        """)
    suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<NoteEntity>

    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNumNotes(): Int
}













