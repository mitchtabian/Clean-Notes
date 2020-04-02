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

    suspend fun insertNewNote(note: Note): Long {
        return noteRepository.insert(note)
    }
}

class DeleteNote(
    private val noteRepository: NoteRepository
){

    suspend fun deleteNote(note: Note): Int {
        return noteRepository.delete(note)
    }
}

class UpdateNote(
    private val noteRepository: NoteRepository
){

    suspend fun updateNote(note: Note): Int {
        return noteRepository.update(note)
    }
}

class GetNotes(
    private val noteRepository: NoteRepository
){

    suspend fun getNotes(): List<Note> {
        return noteRepository.get()
    }
}













