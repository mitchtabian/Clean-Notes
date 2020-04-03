package com.codingwithmitch.cleannotes.notes.business.interactors

import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.DeleteNote
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.SearchNotes
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.InsertNewNote
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.UpdateNote

// Use cases
class NoteInteractors (
    val insertNewNote: InsertNewNote,
    val deleteNote: DeleteNote,
    val updateNote: UpdateNote,
    val searchNotes: SearchNotes
)














