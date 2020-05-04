package com.codingwithmitch.cleannotes.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.state.*
import com.codingwithmitch.cleannotes.business.domain.util.DateUtil
import com.codingwithmitch.cleannotes.business.util.safeApiCall
import com.codingwithmitch.cleannotes.business.util.safeCacheCall
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// For testing
class InsertMultipleNotes(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
){

    fun insertNotes(
        numNotes: Int,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val noteList = NoteListTester.generateNoteList(numNotes)
        safeCacheCall(IO){
            noteCacheDataSource.insertNotes(noteList)
        }

        emit(
            DataState.data<NoteListViewState>(
                response = Response(
                    message = "success",
                    uiComponentType = UIComponentType.None(),
                    messageType = MessageType.None()
                ),
                data = null,
                stateEvent = stateEvent
            )
        )

        updateNetwork(noteList)
    }

    private suspend fun updateNetwork(noteList: List<Note>){
        safeApiCall(IO){
            noteNetworkDataSource.insertOrUpdateNotes(noteList)
        }
    }

}


private object NoteListTester {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val dateUtil =
        DateUtil(dateFormat)

    fun generateNoteList(numNotes: Int): List<Note>{
        val list: ArrayList<Note> = ArrayList()
        for(id in 0..numNotes){
            list.add(generateNote())
        }
        return list
    }

    fun generateNote(): Note {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
        return note
    }
}