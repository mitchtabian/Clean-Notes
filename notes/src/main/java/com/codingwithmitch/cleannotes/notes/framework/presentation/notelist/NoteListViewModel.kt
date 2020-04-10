package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist

import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.cleannotes.core.framework.BaseViewModel
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.interactors.NoteListInteractors
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NOTE_FILTER_DATE_UPDATED
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NOTE_ORDER_DESC
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NoteFactory
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListStateEvent.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


const val DELETE_PENDING_ERROR = "There is already a pending delete operation."
const val NOTE_PENDING_DELETE_BUNDLE_KEY = "pending_delete"

@ExperimentalCoroutinesApi
@FlowPreview
@FeatureScope
class NoteListViewModel
@Inject
constructor(
    private val noteInteractors: NoteListInteractors,
    private val noteFactory: NoteFactory
): BaseViewModel<NoteListViewState>(){


    override fun handleNewData(data: NoteListViewState) {

        data.let { viewState ->
            viewState.noteList?.let { noteList ->
                setNoteListData(noteList)
            }

            viewState.numNotesInCache?.let { numNotes ->
                setNumNotesInCache(numNotes)
            }

            viewState.newNote?.let { note ->
                setNote(note)
            }
        }

    }

    override fun setStateEvent(stateEvent: StateEvent) {

        if(!isJobAlreadyActive(stateEvent)){
            val job: Flow<DataState<NoteListViewState>> = when(stateEvent){

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

                is GetNumNotesInCacheEvent -> {
                    noteInteractors.getNumNotes.getNumNotes(
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

    override fun initNewViewState(): NoteListViewState {
        return NoteListViewState()
    }

    private fun getFilter(): String {
        return getCurrentViewStateOrNew().filter
            ?: NOTE_FILTER_DATE_UPDATED
    }

    private fun getOrder(): String {
        return getCurrentViewStateOrNew().order
            ?: NOTE_ORDER_DESC
    }

    private fun getSearchQuery(): String {
        return getCurrentViewStateOrNew().searchQuery
            ?: return ""
    }

    private fun getPage(): Int{
        return getCurrentViewStateOrNew().page
            ?: return 1
    }

    private fun setNoteListData(notesList: ArrayList<Note>){
        val update = getCurrentViewStateOrNew()
        update.noteList = notesList
        setViewState(update)
    }

    fun setQueryExhausted(isExhausted: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isQueryExhausted = isExhausted
        setViewState(update)
    }

    private fun clearLayoutManagerState(){
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = null
        setViewState(update)
    }

    fun removePendingNoteFromList(){
        val update = getCurrentViewStateOrNew()
        val pendingNote = update.notePendingDelete
        val list = update.noteList
        if(list?.contains(pendingNote) == true){
            list.remove(pendingNote)
            update.noteList = list
            setViewState(update)
        }
    }

    // can be selected from Recyclerview or created new from dialog
    fun setNote(note: Note?){
        val update = getCurrentViewStateOrNew()
        update.newNote = note
        setViewState(update)
    }

    fun isDeletePending(): Boolean{
        if(isJobAlreadyActive(DeleteNoteEvent(primaryKey = 0))){
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = DELETE_PENDING_ERROR,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
            return true
        }
        else{
            return false
        }
    }

    fun onCompleteDelete(){
        setNotePendingDelete(null)
    }

    fun beginPendingDelete(){
        // remove from viewstate
        val update = getCurrentViewStateOrNew()
        update.notePendingDelete?.let { note ->
            update.noteList?.remove(note)
            setViewState(update)
            setStateEvent(DeleteNoteEvent(note.id))
        }

    }

    fun undoDelete(){
        // replace note in viewstate
        val update = getCurrentViewStateOrNew()
        update.notePendingDelete?.let {
            setNotePendingDelete(null)
            update.noteList?.add(it)
        }
        setViewState(update)
    }


    fun setNotePendingDelete(note: Note?){
        val update = getCurrentViewStateOrNew()
        update.notePendingDelete = note
        setViewState(update)
    }

    private fun setNumNotesInCache(numNotes: Int){
        val update = getCurrentViewStateOrNew()
        update.numNotesInCache = numNotes
        setViewState(update)
    }

    fun createNewNote(
        id: Int = -1,
        title: String,
        body: String? = null
    ) = noteFactory.create(id, title, body)

    fun getNoteListSize() = getCurrentViewStateOrNew().noteList?.size?: 0

    fun getNumNotesInCache() = getCurrentViewStateOrNew().numNotesInCache?: 0

    fun isPaginationExhausted() = getNoteListSize() >= getNumNotesInCache()

    fun resetPage(){
        val update = getCurrentViewStateOrNew()
        update.page = 1
        setViewState(update)
    }

    fun isQueryExhausted() = getCurrentViewStateOrNew().isQueryExhausted?: true

    fun loadFirstPage() {
        if(!isJobAlreadyActive(SearchNotesEvent())){
            setQueryExhausted(false)
            resetPage()
            setStateEvent(SearchNotesEvent())
            printLogD("NoteListViewModel",
                "loadFirstPage: ${getCurrentViewStateOrNew().searchQuery}")
        }
    }

    private fun incrementPageNumber(){
        val update = getCurrentViewStateOrNew()
        val page = update.copy().page ?: 1
        update.page = page.plus(1)
        setViewState(update)
    }

    fun nextPage(){
        if(!isJobAlreadyActive(SearchNotesEvent())
            && !getCurrentViewStateOrNew().isQueryExhausted!!
        ){
            printLogD("NoteListViewModel", "attempting to load next page...")
            incrementPageNumber()
            setStateEvent(SearchNotesEvent())
        }
    }

}












































