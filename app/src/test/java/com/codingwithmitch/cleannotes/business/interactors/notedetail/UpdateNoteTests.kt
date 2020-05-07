package com.codingwithmitch.cleannotes.business.interactors.notedetail

import com.codingwithmitch.cleannotes.business.data.cache.CacheErrors.CACHE_ERROR_UNKNOWN
import com.codingwithmitch.cleannotes.business.data.cache.FORCE_UPDATE_NOTE_EXCEPTION
import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.interactors.notedetail.UpdateNote.Companion.UPDATE_NOTE_FAILED
import com.codingwithmitch.cleannotes.business.interactors.notedetail.UpdateNote.Companion.UPDATE_NOTE_SUCCESS
import com.codingwithmitch.cleannotes.business.domain.state.DataState
import com.codingwithmitch.cleannotes.di.DependencyContainer
import com.codingwithmitch.cleannotes.framework.presentation.notedetail.state.NoteDetailStateEvent.*
import com.codingwithmitch.cleannotes.framework.presentation.notedetail.state.NoteDetailViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. updateNote_success_confirmNetworkAndCacheUpdated()
    a) select a random note from the cache
    b) update that note
    c) confirm UPDATE_NOTE_SUCCESS msg is emitted from flow
    d) confirm note is updated in network
    e) confirm note is updated in cache
2. updateNote_fail_confirmNetworkAndCacheUnchanged()
    a) attempt to update a note, fail since does not exist
    b) check for failure message from flow emission
    c) confirm nothing was updated in the cache
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) attempt to update a note, force an exception to throw
    b) check for failure message from flow emission
    c) confirm nothing was updated in the cache
 */
@InternalCoroutinesApi
class UpdateNoteTest {

    // system in test
    private val updateNote: UpdateNote

    // dependencies
    private val dependencyContainer: DependencyContainer
    private val noteCacheDataSource: NoteCacheDataSource
    private val noteNetworkDataSource: NoteNetworkDataSource
    private val noteFactory: NoteFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        noteCacheDataSource = dependencyContainer.noteCacheDataSource
        noteNetworkDataSource = dependencyContainer.noteNetworkDataSource
        noteFactory = dependencyContainer.noteFactory
        updateNote = UpdateNote(
            noteCacheDataSource = noteCacheDataSource,
            noteNetworkDataSource = noteNetworkDataSource
        )
    }


    @Test
    fun updateNote_success_confirmNetworkAndCacheUpdated() =  runBlocking {

        // select a random note to update
        val randomNote = noteCacheDataSource
            .searchNotes("", "", 1).get(0)
        val updatedNote = noteFactory.createSingleNote(
            id = randomNote.id,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )
        updateNote.updateNote(
            note = updatedNote,
            stateEvent = UpdateNoteEvent()
        ).collect(object: FlowCollector<DataState<NoteDetailViewState>?>{
            override suspend fun emit(value: DataState<NoteDetailViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    UPDATE_NOTE_SUCCESS
                )
            }
        })

        // confirm network was updated
        val networkNoteThatWasUpdated = noteNetworkDataSource.searchNote(updatedNote)
        assertTrue { networkNoteThatWasUpdated == updatedNote }

        // confirm cache was updated
        val cacheNoteThatWasUpdated = noteCacheDataSource.searchNoteById(updatedNote.id)
        assertTrue { cacheNoteThatWasUpdated == updatedNote }
    }

    @Test
    fun updateNote_fail_confirmNetworkAndCacheUnchanged() =  runBlocking {

        // create a note to delete that doesn't exist in data set
        val noteToUpdate = Note(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            updated_at = UUID.randomUUID().toString(),
            created_at = UUID.randomUUID().toString()
        )
        updateNote.updateNote(
            note = noteToUpdate,
            stateEvent = UpdateNoteEvent()
        ).collect(object: FlowCollector<DataState<NoteDetailViewState>?>{
            override suspend fun emit(value: DataState<NoteDetailViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    UPDATE_NOTE_FAILED
                )
            }
        })

        // confirm nothing was updated in the cache
        val wasNoteUpdated = noteCacheDataSource.searchNoteById(noteToUpdate.id) == null
        assertTrue { wasNoteUpdated }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking{

        // create a note to update that will throw exception
        val noteToUpdate = Note(
            id = FORCE_UPDATE_NOTE_EXCEPTION ,
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            updated_at = UUID.randomUUID().toString(),
            created_at = UUID.randomUUID().toString()
        )


        updateNote.updateNote(
            noteToUpdate,
            UpdateNoteEvent()
        ).collect(object: FlowCollector<DataState<NoteDetailViewState>?>{
            override suspend fun emit(value: DataState<NoteDetailViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        // confirm nothing was updated in the cache
        val wasNoteUpdated = noteCacheDataSource.searchNoteById(noteToUpdate.id) == null
        assertTrue { wasNoteUpdated }
    }

}


