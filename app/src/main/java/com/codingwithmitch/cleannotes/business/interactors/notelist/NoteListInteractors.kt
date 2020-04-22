package com.codingwithmitch.cleannotes.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.interactors.common.DeleteNote


// Use cases
class NoteListInteractors (
    val insertNewNote: InsertNewNote,
    val deleteNote: DeleteNote<NoteListViewState>,
    val searchNotes: SearchNotes,
    val getNumNotes: GetNumNotes,
    val restoreDeletedNote: RestoreDeletedNote,
    val deleteMultipleNotes: DeleteMultipleNotes,
    val insertMultipleNotes: InsertMultipleNotes // for testing
)














