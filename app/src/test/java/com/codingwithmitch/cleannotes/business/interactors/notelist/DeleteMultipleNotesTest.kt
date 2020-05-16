package com.codingwithmitch.cleannotes.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.data.cache.FORCE_DELETE_NOTE_EXCEPTION
import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.domain.state.DataState
import com.codingwithmitch.cleannotes.business.interactors.notelist.DeleteMultipleNotes.Companion.DELETE_NOTES_ERRORS
import com.codingwithmitch.cleannotes.business.interactors.notelist.DeleteMultipleNotes.Companion.DELETE_NOTES_SUCCESS
import com.codingwithmitch.cleannotes.di.DependencyContainer
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListStateEvent
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListStateEvent.*
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewState
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList


/*
Test cases:
1. deleteNotes_success_confirmNetworkAndCacheUpdated()
    a) select a handful of random notes for deleting
    b) delete from cache and network
    c) confirm DELETE_NOTES_SUCCESS msg is emitted from flow
    d) confirm notes are delted from cache
    e) confirm notes are deleted from "notes" node in network
    f) confirm notes are added to "deletes" node in network
2. deleteNotes_fail_confirmCorrectDeletesMade()
    - This is a complex one:
        - The use-case will attempt to delete all notes passed as input. If there
        is an error with a particular delete, it continues with the others. But the
        resulting msg is DELETE_NOTES_ERRORS. So we need to do rigorous checks here
        to make sure the correct notes were deleted and the correct notes were not.
    a) select a handful of random notes for deleting
    b) change the ids of a few notes so they will cause errors when deleting
    c) confirm DELETE_NOTES_ERRORS msg is emitted from flow
    d) confirm ONLY the valid notes are deleted from network "notes" node
    e) confirm ONLY the valid notes are inserted into network "deletes" node
    f) confirm ONLY the valid notes are deleted from cache
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) select a handful of random notes for deleting
    b) force an exception to be thrown on one of them
    c) confirm DELETE_NOTES_ERRORS msg is emitted from flow
    d) confirm ONLY the valid notes are deleted from network "notes" node
    e) confirm ONLY the valid notes are inserted into network "deletes" node
    f) confirm ONLY the valid notes are deleted from cache
 */
@InternalCoroutinesApi
class DeleteMultipleNotesTest {


    // system in test
    private var deleteMultipleNotes: DeleteMultipleNotes? = null

    // dependencies
    private lateinit var dependencyContainer: DependencyContainer
    private lateinit var noteCacheDataSource: NoteCacheDataSource
    private lateinit var noteNetworkDataSource: NoteNetworkDataSource
    private lateinit var noteFactory: NoteFactory

    @AfterEach
    fun afterEach(){
        deleteMultipleNotes = null
    }

    @BeforeEach
    fun beforeEach(){
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        noteCacheDataSource = dependencyContainer.noteCacheDataSource
        noteNetworkDataSource = dependencyContainer.noteNetworkDataSource
        noteFactory = dependencyContainer.noteFactory
        deleteMultipleNotes = DeleteMultipleNotes(
            noteCacheDataSource = noteCacheDataSource,
            noteNetworkDataSource = noteNetworkDataSource
        )
    }

    @Test
    fun deleteNotes_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val randomNotes: ArrayList<Note> = ArrayList()
        val notesInCache = noteCacheDataSource.searchNotes("","",1)

        for(note in notesInCache){
            randomNotes.add(note)
            if(randomNotes.size > 4){
                break
            }
        }

        deleteMultipleNotes?.deleteNotes(
            notes = randomNotes,
            stateEvent = DeleteMultipleNotesEvent(randomNotes)
        )?.collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {

                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_NOTES_SUCCESS
                )
            }
        })


        // confirm notes are delted from cache
        for(note in randomNotes){
            val noteInCache = noteCacheDataSource.searchNoteById(note.id)
            assertTrue {noteInCache == null}
        }

        // confirm notes are deleted from "notes" node in network
        val doNotesExistInNetwork = noteNetworkDataSource.getAllNotes()
            .containsAll(randomNotes)
        assertFalse {doNotesExistInNetwork}


        // confirm notes are added to "deletes" node in network
        val deletedNetworkNotes = noteNetworkDataSource.getDeletedNotes()
        assertTrue { deletedNetworkNotes.containsAll(randomNotes)}

    }


    @Test
    fun deleteNotes_fail_confirmCorrectDeletesMade() = runBlocking {

        val validNotes: ArrayList<Note> = ArrayList()
        val invalidNotes: ArrayList<Note> = ArrayList()
        val notesInCache = noteCacheDataSource.searchNotes("", "", 1)
        for(index in 0..notesInCache.size){
            var note: Note
            if(index % 2 == 0){
                note = noteFactory.createSingleNote(
                    id = UUID.randomUUID().toString(),
                    title = notesInCache.get(index).title,
                    body = notesInCache.get(index).body
                )
                invalidNotes.add(note)
            }
            else{
                note = notesInCache.get(index)
                validNotes.add(note)
            }
            if((invalidNotes.size + validNotes.size) > 4){
                break
            }
        }

        val notesToDelete = ArrayList(validNotes + invalidNotes)
        deleteMultipleNotes?.deleteNotes(
            notes = notesToDelete,
            stateEvent = DeleteMultipleNotesEvent(notesToDelete)
        )?.collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {

                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_NOTES_ERRORS
                )
            }
        })


        // confirm ONLY the valid notes are deleted from network "notes" node
        val networkNotes = noteNetworkDataSource.getAllNotes()
        assertFalse { networkNotes.containsAll(validNotes)}

        // confirm ONLY the valid notes are inserted into network "deletes" node
        val deletedNetworkNotes = noteNetworkDataSource.getDeletedNotes()
        assertTrue { deletedNetworkNotes.containsAll(validNotes) }
        assertFalse { deletedNetworkNotes.containsAll(invalidNotes) }


        // confirm ONLY the valid notes are deleted from cache
        for(note in validNotes){
            val noteInCache = noteCacheDataSource.searchNoteById(note.id)
            assertTrue {noteInCache == null}
        }
        val numNotesInCache = noteCacheDataSource.getNumNotes()
        assertTrue { numNotesInCache == (notesInCache.size - validNotes.size) }

    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val validNotes: ArrayList<Note> = ArrayList()
        val invalidNotes: ArrayList<Note> = ArrayList()
        val notesInCache = noteCacheDataSource.searchNotes("", "", 1)
        for(note in notesInCache){
            validNotes.add(note)
            if(validNotes.size > 4){
                break
            }
        }

        val errorNote = Note(
            id = FORCE_DELETE_NOTE_EXCEPTION,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            created_at = UUID.randomUUID().toString(),
            updated_at = UUID.randomUUID().toString()
        )
        invalidNotes.add(errorNote)

        val notesToDelete = ArrayList(validNotes + invalidNotes)
        deleteMultipleNotes?.deleteNotes(
            notes = notesToDelete,
            stateEvent = DeleteMultipleNotesEvent(notesToDelete)
        )?.collect(object: FlowCollector<DataState<NoteListViewState>?>{
            override suspend fun emit(value: DataState<NoteListViewState>?) {

                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_NOTES_ERRORS
                )
            }
        })


        // confirm ONLY the valid notes are deleted from network "notes" node
        val networkNotes = noteNetworkDataSource.getAllNotes()
        assertFalse { networkNotes.containsAll(validNotes)}

        // confirm ONLY the valid notes are inserted into network "deletes" node
        val deletedNetworkNotes = noteNetworkDataSource.getDeletedNotes()
        assertTrue { deletedNetworkNotes.containsAll(validNotes) }
        assertFalse { deletedNetworkNotes.containsAll(invalidNotes) }


        // confirm ONLY the valid notes are deleted from cache
        for(note in validNotes){
            val noteInCache = noteCacheDataSource.searchNoteById(note.id)
            assertTrue {noteInCache == null}
        }
        val numNotesInCache = noteCacheDataSource.getNumNotes()
        assertTrue { numNotesInCache == (notesInCache.size - validNotes.size) }
    }

}



































