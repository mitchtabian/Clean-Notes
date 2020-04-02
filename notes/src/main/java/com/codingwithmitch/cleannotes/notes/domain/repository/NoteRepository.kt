package com.codingwithmitch.cleannotes.notes.domain.repository

import com.codingwithmitch.cleannotes.notes.domain.model.Note


interface NoteRepository {

    suspend fun insertNewNote(title: String, body: String): Long

    suspend fun deleteNote(primaryKey: Int): Int

    suspend fun updateNote(note: Note, newTitle: String?, newBody: String?): Int

    suspend fun get(): List<Note>

}