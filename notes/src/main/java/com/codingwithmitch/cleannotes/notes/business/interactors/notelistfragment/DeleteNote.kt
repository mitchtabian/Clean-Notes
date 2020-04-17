package com.codingwithmitch.cleannotes.notes.business.interactors.notelistfragment

import com.codingwithmitch.cleannotes.core.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.core.business.safeCacheCall
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.notes.business.domain.repository.NoteRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteNote<ViewState>(
    private val noteRepository: NoteRepository
){

    fun deleteNote(
        primaryKey: Int,
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>> = flow {


        val cacheResult = safeCacheCall(IO){
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
                                uiComponentType = UIComponentType.None(),
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

    companion object{
        val DELETE_NOTE_SUCCESS = "Successfully deleted note."
        val DELETE_NOTE_PENDING = "Delete pending..."
        val DELETE_NOTE_FAILED = "Failed to delete note."
        val DELETE_NOTE_FAILED_NO_PRIMARY_KEY = "Could not delete that note. No primary key found."
        val DELETE_ARE_YOU_SURE = "Are you sure you want to delete this?\nThis action can't be undone."
        val DELETE_UNDO_TIMEOUT = 3000L
        val DELETE_UNDO = "Undo delete"
    }
}













