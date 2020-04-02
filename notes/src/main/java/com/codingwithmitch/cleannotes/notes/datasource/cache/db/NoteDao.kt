package com.codingwithmitch.notes.datasource.cache.db

import androidx.room.*
import com.codingwithmitch.cleannotes.notes.datasource.model.NoteEntity

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    @Query("DELETE FROM notes WHERE id = :primaryKey")
    suspend fun deleteNote(primaryKey: Int): Int

    @Update
    suspend fun updateNote(note: NoteEntity): Int

    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<NoteEntity>
}