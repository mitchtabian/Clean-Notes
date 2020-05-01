package com.codingwithmitch.cleannotes.business.interactors.common

import com.codingwithmitch.cleannotes.business.data.NoteDataFactory
import com.codingwithmitch.cleannotes.business.data.cache.CacheErrors
import com.codingwithmitch.cleannotes.business.data.cache.CacheErrors.CACHE_ERROR_UNKNOWN
import com.codingwithmitch.cleannotes.business.data.cache.FORCE_DELETE_NOTE_EXCEPTION
import com.codingwithmitch.cleannotes.business.data.cache.FakeNoteCacheDataSourceImpl
import com.codingwithmitch.cleannotes.business.data.network.FakeNoteNetworkDataSourceImpl
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.interactors.common.DeleteNote.Companion.DELETE_NOTE_FAILED
import com.codingwithmitch.cleannotes.business.interactors.common.DeleteNote.Companion.DELETE_NOTE_SUCCESS
import com.codingwithmitch.cleannotes.business.state.DataState
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListStateEvent.*
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.HashMap


/*

    Test cases:
    1. deleteNote_success_confirmNetworkUpdated()
        a) delete a note
        b) check for success message from flow emission
        c) confirm note was deleted from "notes" node in network
        d) confirm note was added to "deletes" node in network
    2. deleteNote_fail_confirmNetworkUnchanged()
        a) attempt to delete a note, fail since does not exist
        b) check for failure message from flow emission
        c) confirm network was not changed
    3. throwException_checkGenericError_confirmNetworkUnchanged()
        a) attempt to delete a note, force an exception to throw
        b) check for failure message from flow emission
        c) confirm network was not changed
 */
@InternalCoroutinesApi
class DeleteNoteTest {

    // system in test
    private var deleteNotes: DeleteNote<NoteListViewState>? = null

    private lateinit var noteDataFactory: NoteDataFactory
    private lateinit var noteNetworkDataSource: FakeNoteNetworkDataSourceImpl
    private lateinit var noteCacheDataSource: FakeNoteCacheDataSourceImpl

    // produce a fake data set of notes for the cache and network
    private lateinit var notesData: HashMap<String, Note>

    init {
        this.javaClass.classLoader?.let { classLoader ->
            noteDataFactory = NoteDataFactory(classLoader)

            // fake data set
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            )
        }
    }

    @BeforeEach
    fun before(){
        // create a fake network data source
        noteNetworkDataSource = FakeNoteNetworkDataSourceImpl(
            notesData = notesData,
            deletedNotesData = HashMap()
        )

        // create a fake Cache data source
        noteCacheDataSource = FakeNoteCacheDataSourceImpl(
            notesData = notesData
        )
    }

    @AfterEach
    fun after(){
        deleteNotes = null
    }

    @Test
    fun deleteNote_success_confirmNetworkUpdated() = runBlocking {

        deleteNotes = DeleteNote(
            noteCacheDataSource = noteCacheDataSource,
            noteNetworkDataSource = noteNetworkDataSource
        )

        // choose a note at random to delete
        var noteToDelete: Note? = null
        for(note in notesData.values){
            noteToDelete = note
        }

        (deleteNotes as DeleteNote).deleteNote(
            noteToDelete as Note,
            DeleteNoteEvent(noteToDelete)
        ).collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_NOTE_SUCCESS
                )
            }
        })

        // confirm was deleted from "notes" node
        val wasNoteDeleted = !noteNetworkDataSource.getAllNotes()
            .contains(noteToDelete)
        assertTrue { wasNoteDeleted }

        // confirm was inserted into "deletes" node
        val wasDeletedNoteInserted = noteNetworkDataSource.getDeletedNotes()
            .contains(noteToDelete)
        assertTrue { wasDeletedNoteInserted }
    }

    @Test
    fun deleteNote_fail_confirmNetworkUnchanged() =  runBlocking{
        deleteNotes = DeleteNote(
            noteCacheDataSource = noteCacheDataSource,
            noteNetworkDataSource = noteNetworkDataSource
        )

        // create a note to delete that doesn't exist in data set
        val noteToDelete = Note(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            updated_at = UUID.randomUUID().toString(),
            created_at = UUID.randomUUID().toString()
        )


        (deleteNotes as DeleteNote).deleteNote(
            noteToDelete,
            DeleteNoteEvent(noteToDelete)
        ).collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_NOTE_FAILED
                )
            }
        })

        // confirm nothing was deleted from "notes" node
        val notes = noteNetworkDataSource.getAllNotes()
        assertTrue { notesData.size == notes.size}

        // confirm was NOT inserted into "deletes" node
        val wasDeletedNoteInserted = !noteNetworkDataSource.getDeletedNotes()
            .contains(noteToDelete)
        assertTrue { wasDeletedNoteInserted }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkUnchanged() = runBlocking{
        deleteNotes = DeleteNote(
            noteCacheDataSource = noteCacheDataSource,
            noteNetworkDataSource = noteNetworkDataSource
        )

        // create a note to delete that will throw exception
        val noteToDelete = Note(
            id = FORCE_DELETE_NOTE_EXCEPTION ,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            updated_at = UUID.randomUUID().toString(),
            created_at = UUID.randomUUID().toString()
        )


        (deleteNotes as DeleteNote).deleteNote(
            noteToDelete,
            DeleteNoteEvent(noteToDelete)
        ).collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {
                assertTrue {
                    value?.stateMessage
                        ?.response
                        ?.message
                        ?.contains(CACHE_ERROR_UNKNOWN)?: false
                }
            }
        })

        // confirm nothing was deleted from "notes" node
        val notes = noteNetworkDataSource.getAllNotes()
        assertTrue { notesData.size == notes.size}

        // confirm was NOT inserted into "deletes" node
        val wasDeletedNoteInserted = !noteNetworkDataSource.getDeletedNotes()
            .contains(noteToDelete)
        assertTrue { wasDeletedNoteInserted }
    }

}


























