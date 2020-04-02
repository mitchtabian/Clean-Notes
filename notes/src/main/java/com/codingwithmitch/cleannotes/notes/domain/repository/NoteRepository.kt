package com.codingwithmitch.cleannotes.notes.domain.repository

import com.codingwithmitch.cleannotes.notes.domain.model.Note


interface NoteRepository {

    suspend fun insert(note: Note): Long

    suspend fun delete(note: Note): Int

    suspend fun update(note: Note): Int

    suspend fun get(): List<Note>

}