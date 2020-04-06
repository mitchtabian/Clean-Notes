package com.codingwithmitch.cleannotes.notes.framework.presentation

import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.framework.BaseViewModel
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.interactors.NoteInteractors
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NOTE_FILTER_DATE_UPDATED
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NOTE_ORDER_DESC
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NoteFactory
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteStateEvent.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class NoteViewModel
@Inject
constructor(
    private val noteInteractors: NoteInteractors,
    private val noteFactory: NoteFactory
): BaseViewModel<NoteViewState>(){


    override fun handleNewData(data: NoteViewState) {

        data.noteListViewState.let { viewState ->
            handleIncomingNotesListData(data)

            viewState.isQueryExhausted?.let {
                setQueryExhausted(it)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        if(!isJobAlreadyActive(stateEvent)){
            val job: Flow<DataState<NoteViewState>> = when(stateEvent){

                is InsertNewNoteEvent -> {
                    noteInteractors.insertNewNote.insertNewNote(
                        title = stateEvent.title,
                        body = stateEvent.body,
                        stateEvent = stateEvent
                    )
                }

                is DeleteNoteEvent -> {
                    noteInteractors.deleteNote.deleteNote(
                        primaryKey = stateEvent.primaryKey,
                        stateEvent = stateEvent
                    )
                }

                is UpdateNoteEvent -> {
                    noteInteractors.updateNote.updateNote(
                        note = stateEvent.note,
                        newTitle = stateEvent.newTitle,
                        newBody = stateEvent.newBody,
                        stateEvent = stateEvent
                    )
                }

                is SearchNotesEvent -> {
                    if(stateEvent.clearLayoutManagerState){
                        clearLayoutManagerState()
                    }
                    noteInteractors.searchNotes.searchNotes(
                        query = getSearchQuery(),
                        filterAndOrder = getOrder() + getFilter(),
                        page = getPage(),
                        stateEvent = stateEvent
                    )
                }

                is CreateStateMessageEvent -> {
                    emitStateMessageEvent(
                        stateMessage = stateEvent.stateMessage,
                        stateEvent = stateEvent
                    )
                }

                else -> {
                    emitInvalidStateEvent(stateEvent)
                }
            }
            launchJob(stateEvent, job)
        }
    }

    override fun initNewViewState(): NoteViewState {
        return NoteViewState()
    }

    private fun getFilter(): String {
        return getCurrentViewStateOrNew().noteListViewState.filter
            ?: NOTE_FILTER_DATE_UPDATED
    }

    private fun getOrder(): String {
        return getCurrentViewStateOrNew().noteListViewState.order
            ?: NOTE_ORDER_DESC
    }

    private fun getSearchQuery(): String {
        return getCurrentViewStateOrNew().noteListViewState.searchQuery
            ?: return ""
    }

    private fun getPage(): Int{
        return getCurrentViewStateOrNew().noteListViewState.page
            ?: return 1
    }

    private fun handleIncomingNotesListData(viewState: NoteViewState){
        viewState.noteListViewState.let { noteListViewState ->
            noteListViewState.noteList?.let { setNoteListData(it) }
        }
    }

    private fun setNoteListData(notesList: List<Note>){
        val update = getCurrentViewStateOrNew()
        update.noteListViewState.noteList = notesList
        setViewState(update)
    }

    private fun setQueryExhausted(isExhausted: Boolean){
        val update = getCurrentViewStateOrNew()
        update.noteListViewState.isQueryExhausted = isExhausted
        setViewState(update)
    }

    private fun clearLayoutManagerState(){
        val update = getCurrentViewStateOrNew()
        update.noteListViewState.layoutManagerState = null
        setViewState(update)
    }

    fun setNote(note: Note){
        val update = getCurrentViewStateOrNew()
        update.noteDetailViewState.note = note
        setViewState(update)
    }

    fun createNewNote(
        id: Int = -1,
        title: String,
        body: String? = null
    ) = noteFactory.create(id, title, body)

}

























