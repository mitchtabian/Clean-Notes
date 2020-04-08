package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail

import androidx.lifecycle.LiveData
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.cleannotes.core.framework.BaseViewModel
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.interactors.NoteDetailInteractors
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.UpdateNote.Companion.UPDATE_NOTE_FAILED_PK
import com.codingwithmitch.cleannotes.notes.framework.datasource.model.NoteEntity
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteDetailStateEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


const val NOTE_DETAIL_ERROR_RETRIEVEING_SELECTED_NOTE = "Error retrieving selected note from bundle."
const val NOTE_DETAIL_SELECTED_NOTE_BUNDLE_KEY = "selectedNote"

@ExperimentalCoroutinesApi
@FlowPreview
@FeatureScope
class NoteDetailViewModel
@Inject
constructor(
    private val noteInteractors: NoteDetailInteractors
): BaseViewModel<NoteDetailViewState>(){

    private val noteInteractionManager: NoteInteractionManager =
        NoteInteractionManager()
    val noteTitleInteractionState: LiveData<NoteInteractionState>
        get() = noteInteractionManager.noteTitleState
    val noteBodyInteractionState: LiveData<NoteInteractionState>
        get() = noteInteractionManager.noteBodyState
    val collapsingToolbarState: LiveData<CollapsingToolbarState>
        get() = noteInteractionManager.collapsingToolbarState


    override fun handleNewData(data: NoteDetailViewState) {
        // no data coming in from requests...
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        if(!isJobAlreadyActive(stateEvent)){
            val job: Flow<DataState<NoteDetailViewState>> = when(stateEvent){

                is DeleteNoteEvent -> {
                    noteInteractors.deleteNote.deleteNote(
                        primaryKey = stateEvent.primaryKey,
                        stateEvent = stateEvent
                    )
                }

                is UpdateNoteEvent -> {
                    printLogD("DetailViewModel", "UpdateNoteEvent")
                    getCurrentViewStateOrNew().note?.id?.let{ pk ->
                        printLogD("DetailViewModel", "attempting update: ${stateEvent.newTitle}")
                        printLogD("DetailViewModel", "attempting update: ${stateEvent.newBody}")
                        printLogD("DetailViewModel", "attempting update: ${noteInteractors.updateNote}")

                        noteInteractors.updateNote.updateNote(
                            primaryKey = pk,
                            newTitle = stateEvent.newTitle,
                            newBody = stateEvent.newBody,
                            stateEvent = stateEvent
                        )
                    }?: emitStateMessageEvent(
                        stateMessage = StateMessage(
                            response = Response(
                                message = UPDATE_NOTE_FAILED_PK,
                                uiComponentType = UIComponentType.Dialog(),
                                messageType = MessageType.Error()
                            )
                        ),
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

    override fun initNewViewState(): NoteDetailViewState {
        return NoteDetailViewState()
    }

    fun setNote(note: Note?){
        val update = getCurrentViewStateOrNew()
        update.note = note
        setViewState(update)
    }

    fun setCollapsingToolbarState(
        state: CollapsingToolbarState
    ) = noteInteractionManager.setCollapsingToolbarState(state)

    fun updateNote(title: String?, body: String?){
        updateNoteTitle(title)
        updateNoteBody(body)
    }

    private fun updateNoteTitle(title: String?){
        if(title == null){
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = NoteEntity.nullTitleError(),
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        )
                    )
                )
            )
        }
        else{
            val update = getCurrentViewStateOrNew()
            val updatedNote = update.note?.copy(
                title = title
            )
            update.note = updatedNote
            setViewState(update)
        }
    }

    private fun updateNoteBody(body: String?){
        val update = getCurrentViewStateOrNew()
        val updatedNote = update.note?.copy(
            body = body?: ""
        )
        update.note = updatedNote
        setViewState(update)
    }

    fun setNoteInteractionTitleState(state: NoteInteractionState){
        noteInteractionManager.setNewNoteTitleState(state)
    }

    fun setNoteInteractionBodyState(state: NoteInteractionState){
        noteInteractionManager.setNewNoteBodyState(state)
    }

    // return true if in EditState
    fun checkEditState() = noteInteractionManager.checkEditState()

    fun exitEditState() = noteInteractionManager.exitEditState()

    fun isEditingTitle() = noteInteractionManager.isEditingTitle()

    fun isEditingBody() = noteInteractionManager.isEditingBody()

    // force observers to refresh
    fun triggerNoteObservers(){
        getCurrentViewStateOrNew().note?.let { note ->
            setNote(note)
        }
    }
}












































