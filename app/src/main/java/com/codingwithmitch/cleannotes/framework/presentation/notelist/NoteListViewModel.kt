package com.codingwithmitch.cleannotes.framework.presentation.notelist

import android.content.SharedPreferences
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.domain.state.StateEvent
import com.codingwithmitch.cleannotes.business.interactors.notelist.NoteListInteractors
import com.codingwithmitch.cleannotes.framework.presentation.common.BaseViewModel
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class NoteListViewModel
constructor(
    private val noteListInteractors: NoteListInteractors,
    private val noteFactory: NoteFactory,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
): BaseViewModel<NoteListViewState>(){

    override fun handleNewData(data: NoteListViewState) {

    }

    override fun setStateEvent(stateEvent: StateEvent) {
    }

    override fun initNewViewState(): NoteListViewState {
        return NoteListViewState()
    }

}