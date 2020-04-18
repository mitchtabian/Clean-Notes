package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListToolbarState.*

class NoteListInteractionManager {

    private val _toolbarState: MutableLiveData<NoteListToolbarState>
            = MutableLiveData(SearchViewState())

    val toolbarState: LiveData<NoteListToolbarState>
            get() = _toolbarState

    fun setToolbarState(state: NoteListToolbarState){
        _toolbarState.value = state
    }

    fun isMultiSelectionStateActive()
            = (_toolbarState.value == MultiSelectionState())
}















