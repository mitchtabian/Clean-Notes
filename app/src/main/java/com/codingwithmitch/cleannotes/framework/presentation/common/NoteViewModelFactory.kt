package com.codingwithmitch.cleannotes.framework.presentation.common

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.interactors.notedetail.NoteDetailInteractors
import com.codingwithmitch.cleannotes.business.interactors.notelist.NoteListInteractors
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class NoteViewModelFactory
@Inject
constructor(
    private val noteListInteractors: NoteListInteractors,
    private val noteDetailInteractors: NoteDetailInteractors,
    private val noteFactory: NoteFactory,
    private val editor: SharedPreferences.Editor,
    sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when(modelClass){

           is NoteListViewModel
        }
    }
}






















