package com.codingwithmitch.cleannotes.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.data.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.state.*
import com.codingwithmitch.cleannotes.business.util.safeApiCall
import com.codingwithmitch.cleannotes.business.util.safeCacheCall
import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.network.ApiResponseHandler
import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.framework.presentation.notedetail.state.NoteDetailViewState
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewState
import com.codingwithmitch.cleannotes.util.printLogD
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertNewNote(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource,
    private val noteFactory: NoteFactory
){

    fun insertNewNote(
        id: String? = null,
        title: String,
        body: String,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val newNote = noteFactory.createSingleNote(
            id = id ?: UUID.randomUUID().toString(),
            title = title,
            body = body
        )
        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.insertNote(newNote)
        }

        val cacheResponse = object: CacheResponseHandler<NoteListViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<NoteListViewState>? {
                return if(resultObj > 0){
                    val viewState =
                        NoteListViewState(
                            newNote = newNote
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

        emit(cacheResponse)

        // update network
        // TODO("WorkManager???")
        if(cacheResponse?.stateMessage?.response?.message.equals(INSERT_NOTE_SUCCESS)){

            // not listening for success/failure here b/c we don't take any action either way
            val apiResult = safeApiCall(IO){
                noteNetworkDataSource.insertOrUpdateNote(newNote)
            }

            object: ApiResponseHandler<NoteDetailViewState, Task<Void>>(
                response = apiResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Task<Void>): DataState<NoteDetailViewState>? {
                    resultObj.addOnFailureListener {

                        // Good place to send error report
                        printLogD("InsertNote",
                            "Network: onFailure: ${it.message}")

                        printLogD("InsertNote",
                            "Network: onFailure: ${it.cause}")

                        printLogD("InsertNote",
                            "Network: onFailure: ${it}")
                    }
                    return null
                }

            }.getResult()

        }
    }

    companion object{
        val INSERT_NOTE_SUCCESS = "Successfully inserted new note."
        val INSERT_NOTE_FAILED = "Failed to insert new note."
    }
}