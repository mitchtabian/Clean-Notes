package com.codingwithmitch.cleannotes.notes.business.interactors.notelistfragment

import com.codingwithmitch.cleannotes.core.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.core.business.safeCacheCall
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.notes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumNotes(
    private val noteRepository: NoteRepository
){

    fun getNumNotes(
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteRepository.getNumNotes()
        }

        emit(
            object: CacheResponseHandler<NoteListViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<NoteListViewState> {
                    val viewState = NoteListViewState(
                        numNotesInCache = resultObj
                    )
                    return DataState.data(
                        response = Response(
                            message = GET_NUM_NOTES_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

    companion object{
        val GET_NUM_NOTES_SUCCESS = "Successfully retrieved the number of notes from the cache."
        val GET_NUM_NOTES_FAILED = "Failed to get the number of notes from the cache."
    }
}