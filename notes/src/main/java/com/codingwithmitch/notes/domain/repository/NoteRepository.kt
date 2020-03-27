package com.codingwithmitch.notes.domain.repository

import com.codingwithmitch.notes.domain.model.Note

interface NoteRepository {

    suspend fun insert(note: Note): Long

    suspend fun delete(note: Note): Int

    suspend fun update(note: Note): Int

    suspend fun get(): List<Note>

}