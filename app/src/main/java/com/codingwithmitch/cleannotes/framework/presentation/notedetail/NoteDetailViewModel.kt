package com.codingwithmitch.cleannotes.framework.presentation.notedetail

import com.codingwithmitch.cleannotes.business.domain.state.StateEvent
import com.codingwithmitch.cleannotes.business.interactors.notedetail.NoteDetailInteractors
import com.codingwithmitch.cleannotes.framework.presentation.common.BaseViewModel
import com.codingwithmitch.cleannotes.framework.presentation.notedetail.state.NoteDetailViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class NoteDetailViewModel
@Inject
constructor(
    private val noteDetailInteractors: NoteDetailInteractors
): BaseViewModel<NoteDetailViewState>(){

    override fun handleNewData(data: NoteDetailViewState) {

    }

    override fun setStateEvent(stateEvent: StateEvent) {

    }

    override fun initNewViewState(): NoteDetailViewState {
        return NoteDetailViewState()
    }

}




















