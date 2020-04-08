package com.codingwithmitch.cleannotes.notes.business.interactors.use_cases

import com.codingwithmitch.cleannotes.core.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.core.business.safeCacheCall
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.notes.business.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteNote<ViewState>(
    private val noteRepository: NoteRepository
){

    val DELETE_NOTE_SUCCESS = "Successfully deleted note."
    val DELETE_NOTE_FAILED = "Failed to delete note."

    fun deleteNote(
        primaryKey: Int,
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteRepository.deleteNote(primaryKey)
        }

        emit(
            object: CacheResponseHandler<ViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<ViewState> {
                    return if(resultObj > 0){
                        DataState.data(
                            response = Response(
                                message = DELETE_NOTE_SUCCESS,
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
                                message = DELETE_NOTE_FAILED,
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