package com.codingwithmitch.cleannotes.notes.business.interactors.use_cases

import com.codingwithmitch.cleannotes.core.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.core.business.safeCacheCall
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNote<ViewState>(
    private val noteRepository: NoteRepository
){

    fun updateNote(
        primaryKey: Int,
        newTitle: String,
        newBody: String?,
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>> = flow {

        printLogD("UpdateNote", "creating flow")

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteRepository.updateNote(
                primaryKey = primaryKey,
                newTitle = newTitle,
                newBody = newBody
            )
        }

        emit(
            object: CacheResponseHandler<ViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<ViewState> {
                    return if(resultObj > 0){
                        printLogD("UpdateNote", "SUCCESS")
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
                        printLogD("UpdateNote", "FAIL")
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

    companion object{
        val UPDATE_NOTE_SUCCESS = "Successfully updated note."
        val UPDATE_NOTE_FAILED = "Failed to update note."
        val UPDATE_NOTE_FAILED_PK = "Update failed. Note is missing primary key."

    }
}