package com.codingwithmitch.cleannotes.notes.presentation

import androidx.lifecycle.ViewModel
import com.codingwithmitch.cleannotes.notes.domain.repository.NoteRepository
import javax.inject.Inject

class NoteViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository
): ViewModel(){

    private val TAG: String = "AppDebug"



}

















