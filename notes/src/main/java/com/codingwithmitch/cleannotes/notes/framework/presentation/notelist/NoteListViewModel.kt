package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.cleannotes.core.framework.BaseViewModel
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.interactors.notelistfragment.NoteListInteractors
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NOTE_FILTER_DATE_CREATED
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NOTE_ORDER_DESC
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NoteFactory
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListInteractionManager
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListStateEvent.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListToolbarState
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState.*
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


    val noteListInteractionManager = NoteListInteractionManager()

    val toolbarState: LiveData<NoteListToolbarState>
            get() = noteListInteractionManager.toolbarState

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

            viewState.notePendingDelete?.let { restoredNote ->
                restoredNote.note?.let { note ->
                    setRestoredNoteId(note)
                }
                setNotePendingDelete(null)
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

                is RestoreDeletedNoteEvent -> {
                    noteInteractors.restoreDeletedNote.restoreDeletedNote(
                        note = stateEvent.note,
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

    fun setToolbarState(state: NoteListToolbarState)
            = noteListInteractionManager.setToolbarState(state)

    fun isMultiSelectionStateActive()
            = noteListInteractionManager.isMultiSelectionStateActive()

    override fun initNewViewState(): NoteListViewState {
        return NoteListViewState()
    }

    private fun getFilter(): String {
        return getCurrentViewStateOrNew().filter
            ?: NOTE_FILTER_DATE_CREATED
    }

    private fun getOrder(): String {
        return getCurrentViewStateOrNew().order
            ?: NOTE_ORDER_DESC
    }

    fun getSearchQuery(): String {
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

    // can be selected from Recyclerview or created new from dialog
    fun setNote(note: Note?){
        val update = getCurrentViewStateOrNew()
        update.newNote = note
        setViewState(update)
    }

    fun setQuery(query: String?){
        val update =  getCurrentViewStateOrNew()
        update.searchQuery = query
        setViewState(update)
    }

    // if a note is deleted and then restored, the id will be incorrect.
    // So need to reset it here.
    private fun setRestoredNoteId(restoredNote: Note){
        val update = getCurrentViewStateOrNew()
        update.noteList?.let { noteList ->
            for((index, note) in noteList.withIndex()){
                if(note.title.equals(restoredNote.title)){
                    noteList.remove(note)
                    noteList.add(index, restoredNote)
                    update.noteList = noteList
                    break
                }
            }
        }
        setViewState(update)
    }

    fun isDeletePending(): Boolean{
        val pendingNote = getCurrentViewStateOrNew().notePendingDelete
        if(pendingNote != null){
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

    fun beginPendingDelete(note: Note){
        setNotePendingDelete(note)
        removePendingNoteFromList(note)
        setStateEvent(
            DeleteNoteEvent(
                primaryKey = note.id
            )
        )
    }

    private fun removePendingNoteFromList(note: Note?){
        val update = getCurrentViewStateOrNew()
        val list = update.noteList
        if(list?.contains(note) == true){
            list.remove(note)
            update.noteList = list
            setViewState(update)
        }
    }

    fun undoDelete(){
        // replace note in viewstate
        val update = getCurrentViewStateOrNew()
        update.notePendingDelete?.let { note ->
            if(note.listPosition != null && note.note != null){
                update.noteList?.add(
                    note.listPosition as Int,
                    note.note as Note
                )
                setStateEvent(RestoreDeletedNoteEvent(note.note as Note))
            }
        }
        setViewState(update)
    }


    fun setNotePendingDelete(note: Note?){
        val update = getCurrentViewStateOrNew()
        if(note != null){
            update.notePendingDelete = NotePendingDelete(
                note = note,
                listPosition = findListPositionOfNote(note)
            )
        }
        else{
            update.notePendingDelete = null
        }
        setViewState(update)
    }

    private fun findListPositionOfNote(note: Note?): Int {
        val viewState = getCurrentViewStateOrNew()
        viewState.noteList?.let { noteList ->
            for((index, item) in noteList.withIndex()){
                if(item.id == note?.id){
                    return index
                }
            }
        }
        return 0
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

    private fun getNumNotesInCache() = getCurrentViewStateOrNew().numNotesInCache?: 0

    fun isPaginationExhausted() = getNoteListSize() >= getNumNotesInCache()

    private fun resetPage(){
        val update = getCurrentViewStateOrNew()
        update.page = 1
        setViewState(update)
    }

    // for debugging
    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun isQueryExhausted(): Boolean{
        printLogD("NoteListViewModel",
            "is query exhasuted? ${getCurrentViewStateOrNew().isQueryExhausted?: true}")
        return getCurrentViewStateOrNew().isQueryExhausted?: true
    }

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

    fun restoreFromCache(){
        if(!isJobAlreadyActive(SearchNotesEvent())){
            setQueryExhausted(false)
            setStateEvent(SearchNotesEvent(false))
        }
    }

    fun setLayoutManagerState(layoutManagerState: Parcelable){
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = layoutManagerState
        setViewState(update)
    }

    fun nextPage(){
        if(!isJobAlreadyActive(SearchNotesEvent())
            && !isQueryExhausted()
        ){
            printLogD("NoteListViewModel", "attempting to load next page...")
            incrementPageNumber()
            setStateEvent(SearchNotesEvent())
        }
    }

    fun addOrRemoveNoteFromSelectedList(note: Note)
            = noteListInteractionManager.addOrRemoveNoteFromSelectedList(note)

    fun isNoteSelected(note: Note): Boolean
            = noteListInteractionManager.isNoteSelected(note)

    fun clearSelectedNotes() = noteListInteractionManager.clearSelectedNotes()


}












































