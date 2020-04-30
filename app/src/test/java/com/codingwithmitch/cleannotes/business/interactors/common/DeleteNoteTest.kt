package com.codingwithmitch.cleannotes.business.interactors.common

import com.codingwithmitch.cleannotes.business.data.NoteDataFactory
import com.codingwithmitch.cleannotes.business.data.cache.FakeNoteCacheDataSourceImpl
import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.network.FakeNoteNetworkDataSourceImpl
import com.codingwithmitch.cleannotes.business.state.ViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class DeleteNoteTest {

    // system in test
    private var deleteNotes: DeleteNote<ViewState>? = null

    private lateinit var noteDataFactory: NoteDataFactory
    private lateinit var noteNetworkDataSource: FakeNoteNetworkDataSourceImpl
    private lateinit var noteCacheDataSource: FakeNoteCacheDataSourceImpl

    init {
        this.javaClass.classLoader?.let { classLoader ->
            noteDataFactory = NoteDataFactory(classLoader)

            // produce a fake data set of notes for the cache and network
            val notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            )

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
    }

    @AfterEach
    fun after(){
        deleteNotes = null
    }

    @Test
    fun deleteNote_returnMessageSuccess() = runBlocking{

        deleteNotes = DeleteNote(
            noteCacheDataSource = noteCacheDataSource,
            noteNetworkDataSource = noteNetworkDataSource
        )


    }
}













