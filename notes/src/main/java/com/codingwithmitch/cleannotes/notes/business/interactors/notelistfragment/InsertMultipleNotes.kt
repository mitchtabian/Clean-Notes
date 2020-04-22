package com.codingwithmitch.cleannotes.notes.business.interactors.notelistfragment

import com.codingwithmitch.cleannotes.core.business.DateUtil
import com.codingwithmitch.cleannotes.core.business.safeCacheCall
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers
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
        val cacheResult = safeCacheCall(Dispatchers.IO){
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