package com.codingwithmitch.cleannotes.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.state.*
import com.codingwithmitch.cleannotes.business.util.safeCacheCall
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertNewNote(
    private val noteRepository: NoteRepository,
    private val noteFactory: NoteFactory
){

    fun insertNewNote(
        title: String,
        body: String,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteRepository.insertNewNote(title, body)
        }

        emit(
            object: CacheResponseHandler<NoteListViewState, Long>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Long): DataState<NoteListViewState> {
                    return if(resultObj > 0){
                        val viewState =
                            NoteListViewState(
                                newNote = noteFactory.create(
                                    id = resultObj.toInt(),
                                    title = title,
                                    body = body
                                )
                            )
                        DataState.data(
                            response = Response(
                                message = INSERT_NOTE_SUCCESS,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    }
                    else{
                        DataState.data(
                            response = Response(
                                message = INSERT_NOTE_FAILED,
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
        val INSERT_NOTE_SUCCESS = "Successfully inserted new note."
        val INSERT_NOTE_FAILED = "Failed to insert new note."
    }
}