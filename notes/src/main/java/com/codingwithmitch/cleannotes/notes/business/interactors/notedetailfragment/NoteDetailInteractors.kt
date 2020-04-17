package com.codingwithmitch.cleannotes.notes.business.interactors.notedetailfragment

import com.codingwithmitch.cleannotes.notes.business.interactors.notelistfragment.DeleteNote
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteDetailViewState

// Use cases
class NoteDetailInteractors (
    val deleteNote: DeleteNote<NoteDetailViewState>,
    val updateNote: UpdateNote
)














