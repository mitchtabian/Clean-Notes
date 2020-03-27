package com.codingwithmitch.notes.datasource.cache.db

import androidx.room.*
import com.codingwithmitch.notes.datasource.model.NoteEntity

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    @Delete
    suspend fun deleteNote(note: NoteEntity): Int

    @Update
    suspend fun updateNote(note: NoteEntity): Int

    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<NoteEntity>
}