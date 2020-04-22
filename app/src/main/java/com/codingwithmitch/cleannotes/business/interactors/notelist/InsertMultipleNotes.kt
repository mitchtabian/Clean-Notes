package com.codingwithmitch.cleannotes.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.state.*
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.business.util.safeCacheCall
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewState
import com.codingwithmitch.cleannotes.util.printLogD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// For testing
class InsertMultipleNotes(
    private val noteRepository: NoteRepository
){

    fun insertNotes(
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>> = flow {

        val noteList = NoteListTester.generateNoteList(1000)
        val cacheResult = safeCacheCall(IO){
            noteRepository.insertNotes(noteList)
        }

        printLogD("CacheResult", "${cacheResult}")

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
    }

}


private object NoteListTester {

    private val dateUtil = DateUtil()

    fun generateNoteList(numNotes: Int): List<Note>{
        val list: ArrayList<Note> = ArrayList()
        for(id in 0..numNotes){
            list.add(generateNote(id))
        }
        return list
    }

    fun generateNote(id: Int): Note {
        val note = Note(
            id = id,
            title = id.toString(),
            body = "",
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
        return note
    }
}