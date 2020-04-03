package com.codingwithmitch.cleannotes.notes.business.interactors.use_cases

import com.codingwithmitch.cleannotes.core.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.core.business.safeCacheCall
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.domain.repository.NoteRepository
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteViewState
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteViewState.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchNotes(
    private val noteRepository: NoteRepository
){

    val SEARCH_NOTES_SUCCESS = "Successfully retrieved list of notes."
    val SEARCH_NOTES_NO_MATCHING_RESULTS = "There are no notes that match that query."
    val SEARCH_NOTES_FAILED = "Failed to retrieve the list of notes."

    fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<NoteViewState>> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteRepository.searchNotes(
                query = query,
                filterAndOrder = filterAndOrder,
                page = page
            )
        }

        emit(
            object: CacheResponseHandler<NoteViewState, List<Note>>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: List<Note>): DataState<NoteViewState> {
                    var message: String? = SEARCH_NOTES_SUCCESS
                    var uiComponentType: UIComponentType? = UIComponentType.None()
                    if(resultObj.size == 0){
                        message = SEARCH_NOTES_NO_MATCHING_RESULTS
                        uiComponentType = UIComponentType.Toast()
                    }
                    return DataState.data(
                        response = Response(
                            message = message,
                            uiComponentType = uiComponentType as UIComponentType,
                            messageType = MessageType.Success()
                        ),
                        data = NoteViewState(
                            NoteListViewState(
                                noteList = resultObj
                            )
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }
}