package com.codingwithmitch.cleannotes.notes.business.interactors.use_cases

import com.codingwithmitch.cleannotes.core.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.core.business.safeCacheCall
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.domain.repository.NoteRepository
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNote(
    private val noteRepository: NoteRepository
){

    val UPDATE_NOTE_SUCCESS = "Successfully updated note."
    val UPDATE_NOTE_FAILED = "Failed to update note."

    fun updateNote(
        note: Note,
        newTitle: String?,
        newBody: String?,
        stateEvent: StateEvent
    ): Flow<DataState<NoteViewState>> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteRepository.updateNote(
                note = note,
                newTitle = newTitle,
                newBody = newBody
            )
        }

        emit(
            object: CacheResponseHandler<NoteViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<NoteViewState> {
                    return if(resultObj > 0){
                        DataState.data(
                            response = Response(
                                message = UPDATE_NOTE_SUCCESS,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            data = null,
                            stateEvent = stateEvent
                        )
                    }
                    else{
                        DataState.data(
                            response = Response(
                                message = UPDATE_NOTE_FAILED,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Error()
                            ),
                            data = null,
                            stateEvent = stateEvent
                        )
                    }
                }
            }.getResult()
        )
    }
}