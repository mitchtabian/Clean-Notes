package com.codingwithmitch.cleannotes.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.state.*
import com.codingwithmitch.cleannotes.business.util.safeCacheCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchNotes(
    private val noteRepository: NoteRepository
){

    fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteRepository.searchNotes(
                query = query,
                filterAndOrder = filterAndOrder,
                page = page
            )
        }

        emit(
            object: CacheResponseHandler<NoteListViewState, List<Note>>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: List<Note>): DataState<NoteListViewState> {
                    var message: String? =
                        SEARCH_NOTES_SUCCESS
                    var uiComponentType: UIComponentType? = UIComponentType.None()
                    if(resultObj.size == 0){
                        message =
                            SEARCH_NOTES_NO_MATCHING_RESULTS
                        uiComponentType = UIComponentType.Toast()
                    }
                    return DataState.data(
                        response = Response(
                            message = message,
                            uiComponentType = uiComponentType as UIComponentType,
                            messageType = MessageType.Success()
                        ),
                        data = NoteListViewState(
                            noteList = ArrayList(resultObj)
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

    companion object{
        val SEARCH_NOTES_SUCCESS = "Successfully retrieved list of notes."
        val SEARCH_NOTES_NO_MATCHING_RESULTS = "There are no notes that match that query."
        val SEARCH_NOTES_FAILED = "Failed to retrieve the list of notes."

    }
}







