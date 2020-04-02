package com.codingwithmitch.cleannotes.notes.interactors

import com.codingwithmitch.cleannotes.notes.domain.model.Note
import com.codingwithmitch.cleannotes.notes.domain.repository.NoteRepository

class NoteInteractors (
    val insertNewNote: InsertNewNote,
    val deleteNote: DeleteNote,
    val updateNote: UpdateNote,
    val getNotes: GetNotes
)

class InsertNewNote(
    private val noteRepository: NoteRepository
){

    suspend fun insertNewNote(title: String, body: String): Long {
        return noteRepository.insertNewNote(title, body)
    }
}

class DeleteNote(
    private val noteRepository: NoteRepository
){

    suspend fun deleteNote(primaryKey: Int): Int {
        return noteRepository.deleteNote(primaryKey)
    }
}

class UpdateNote(
    private val noteRepository: NoteRepository
){

    suspend fun updateNote(note: Note, newTitle: String?, newBody: String?): Int {
        return noteRepository.updateNote(note, newTitle, newBody)
    }
}

class GetNotes(
    private val noteRepository: NoteRepository
){

    suspend fun getNotes(): List<Note> {
        return noteRepository.get()
    }
}













