package com.codingwithmitch.cleannotes.notes.presentation

import androidx.lifecycle.ViewModel
import com.codingwithmitch.cleannotes.notes.domain.repository.NoteRepository
import com.codingwithmitch.cleannotes.notes.interactors.*
import javax.inject.Inject

class NoteViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository
): ViewModel(){

    init {
        test()
    }

    private fun test(){
        val interactors = NoteInteractors(
            InsertNewNote(noteRepository),
            DeleteNote(noteRepository),
            UpdateNote(noteRepository),
            GetNotes(noteRepository)
        )



    }
}