package com.codingwithmitch.cleannotes.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.business.interactors.common.DeleteNote.Companion.DELETE_NOTE_FAILED
import com.codingwithmitch.cleannotes.business.interactors.common.DeleteNote.Companion.DELETE_NOTE_SUCCESS
import com.codingwithmitch.cleannotes.business.state.*
import com.codingwithmitch.cleannotes.business.util.safeCacheCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteMultipleNotes(
    private val noteRepository: NoteRepository
){

    /**
     * Logic:
     * 1. execute all the deletes and save result into an ArrayList<DataState<NoteListViewState>>
     * 2a. If one of the results is a failure, emit a failure response
     * 2b. If all success, emit success response
     */
    fun deleteNotes(
        primaryKeys: IntArray,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>> = flow {

        val results: ArrayList<DataState<NoteListViewState>> = ArrayList()
        for(pk in primaryKeys){
            val cacheResult = safeCacheCall(IO){
                noteRepository.deleteNote(pk)
            }
            results.add(
                object: CacheResponseHandler<NoteListViewState, Int>(
                    response = cacheResult,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: Int): DataState<NoteListViewState> {
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

        var deleteNotesSuccess = true
        for (result in results){
            if(result.stateMessage?.response?.message.equals(DELETE_NOTE_FAILED)){
                deleteNotesSuccess = false
                break
            }
        }
        if(deleteNotesSuccess){
            emit(
                DataState.data<NoteListViewState>(
                    response = Response(
                        message = DELETE_NOTES_SUCCESS,
                        uiComponentType = UIComponentType.Toast(),
                        messageType = MessageType.Success()
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        }
        else{
            emit(
                DataState.data<NoteListViewState>(
                    response = Response(
                        message = DELETE_NOTES_FAILED,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Success()
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        }

    }

    companion object{
        val DELETE_NOTES_SUCCESS = "Successfully deleted notes."
        val DELETE_NOTES_FAILED = "Not all the notes you selected were deleted. There was some errors."
        val DELETE_NOTES_YOU_MUST_SELECT = "You haven't selected any notes to delete."
        val DELETE_NOTES_ARE_YOU_SURE = "Are you sure you want to delete these?"
    }
}













