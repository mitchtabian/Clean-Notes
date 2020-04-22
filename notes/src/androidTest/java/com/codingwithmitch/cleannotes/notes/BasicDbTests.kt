package com.codingwithmitch.cleannotes.notes

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.notes.business.data.implementation.NoteRepositoryImpl
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NoteMapper
import com.codingwithmitch.cleannotes.notes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.core.business.DateUtil
import com.codingwithmitch.cleannotes.core.business.state.DataState
import com.codingwithmitch.cleannotes.notes.business.interactors.common.DeleteNote
import com.codingwithmitch.cleannotes.notes.business.interactors.notelistfragment.*
import com.codingwithmitch.cleannotes.notes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListStateEvent.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState
import com.codingwithmitch.notes.datasource.cache.db.NoteDatabase
import com.codingwithmitch.notes.datasource.cache.repository.NoteCacheDataSourceImpl
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * BUG WITH TESTING DYNAMIC FEATURES:
 * https://issuetracker.google.com/issues/145191501
 */
@InternalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class BasicDbTests {


//    private val dateUtil: DateUtil =
//        DateUtil()
//    private lateinit var db: NoteDatabase
//    private lateinit var noteRepository: NoteRepository
//    private lateinit var interactors: NoteListInteractors

//    @Before
//    fun setupInteractors() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(
//            context, NoteDatabase::class.java).build()
//        noteRepository = NoteRepositoryImpl(
//            NoteCacheDataSourceImpl(
//                noteDao = db.noteDao(),
//                noteEntityMapper = NoteMapper(dateUtil),
//                dateUtil = dateUtil
//            )
//        )
//        interactors =
//            NoteListInteractors(
//                InsertNewNote(noteRepository,
//                    NoteFactory(
//                        dateUtil
//                    )
//                ),
//                DeleteNote(noteRepository),
//                SearchNotes(noteRepository),
//                GetNumNotes(noteRepository),
//                RestoreDeletedNote(noteRepository),
//                DeleteMultipleNotes(noteRepository),
//                InsertMultipleNotes(noteRepository)
//            )
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun closeDb() {
//        db.close()
//    }
//
//    @Test
//    fun writeNewNote_confirmInserted() {
//
//        val newTitle = "first note"
//        val newBody = "something I should not forget!"
//        val stateEvent = SearchNotesEvent()
//        runBlocking {
//            interactors
//                .insertNewNote
//                .insertNewNote(
//                    title = newTitle,
//                    body = newBody,
//                    stateEvent = stateEvent
//                )
//        }
//        runBlocking {
//            val notesFlow: Flow<DataState<NoteListViewState>>? = flow{
//                interactors.searchNotes.searchNotes(
//                    query = "",
//                    filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
//                    page = 0,
//                    stateEvent = stateEvent
//                )
//            }
//            notesFlow?.collect(object: FlowCollector<DataState<NoteListViewState>>{
//                override suspend fun emit(value: DataState<NoteListViewState>) {
//
//                    // loop through results and make sure one of them is the new note
//                    val notesList = value.data?.noteList
//                    if(notesList != null){
//                        for((index, note) in notesList.withIndex()){
//                            if(note.title.equals(newTitle)
//                                && note.body.equals(newBody)){
//                                assert(true)
//                                break
//                            }
//                            if(index == notesList.size - 1){
//                                assert(false)
//                                break
//                            }
//                        }
//                    }
//                }
//            })
//        }
//    }
}















