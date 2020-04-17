package com.codingwithmitch.cleannotes.notes.business.interactors.notelistfragment

import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState

// Use cases
class NoteListInteractors (
    val insertNewNote: InsertNewNote,
    val deleteNote: DeleteNote<NoteListViewState>,
    val searchNotes: SearchNotes,
    val getNumNotes: GetNumNotes,
    val restoreDeletedNote: RestoreDeletedNote
)














