package com.codingwithmitch.cleannotes.business.interactors

import com.codingwithmitch.cleannotes.business.domain.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.business.interactors.notelist.SearchNotes
import com.codingwithmitch.cleannotes.business.state.DataState
import com.codingwithmitch.cleannotes.data.NoteDataFactory
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListStateEvent.*
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// test cases:
// 1) Returns data=List<Note>(non-empty) and message=SEARCH_NOTES_SUCCESS
// 2) Returns data=List<Note>(empty) and message=SEARCH_NOTES_NO_MATCHING_RESULTS
// 3) Returns stateMessage=ERROR due to exception

// Testing things like 'making sure correct data is returned given a specific
// query' can be done in Espresso tests when using DAO
@InternalCoroutinesApi
class SearchNotesTest {

    // use-case in test
    private var searchNotes: SearchNotes? = null
    private lateinit var noteFactory: NoteDataFactory

    init {
        this.javaClass.classLoader?.let { classLoader ->
            noteFactory = NoteDataFactory(classLoader)
        }
    }

    @AfterEach
    fun after(){
        searchNotes = null
    }

    // 1) Returns data=List<Note>(non-empty) and message=SEARCH_NOTES_SUCCESS
    @Test
    fun searchNotes_returnList_messageSuccess() = runBlocking {
        val noteRepository = mockk<NoteRepository>()
        val dummyListOfNotes = noteFactory.produceListOfNotes()
        every {
            runBlocking {
                noteRepository.searchNotes(any(), any(), any())
            }
        } returns dummyListOfNotes

        searchNotes = SearchNotes(noteRepository)
        (searchNotes as SearchNotes).searchNotes("", "", 0, SearchNotesEvent())
            .collect(object: FlowCollector<DataState<NoteListViewState>>{
                override suspend fun emit(value: DataState<NoteListViewState>) {

                    assertEquals(
                        value.data?.noteList,
                        dummyListOfNotes
                    )
                    assertEquals(
                        value.stateMessage?.response?.message,
                        SearchNotes.SEARCH_NOTES_SUCCESS
                    )
                }

            })
    }

    // 2) Returns data=List<Note>(empty) and message=SEARCH_NOTES_NO_MATCHING_RESULTS
    @Test
    fun searchNotes_returnEmptyList_messageSuccess() = runBlocking {
        val noteRepository = mockk<NoteRepository>()
        val dummyListOfNotes = noteFactory.produceEmptyListOfNotes()
        every {
            runBlocking {
                noteRepository.searchNotes(any(), any(), any())
            }
        } returns dummyListOfNotes

        searchNotes = SearchNotes(noteRepository)
        (searchNotes as SearchNotes).searchNotes("", "", 0, SearchNotesEvent())
            .collect(object: FlowCollector<DataState<NoteListViewState>>{
                override suspend fun emit(value: DataState<NoteListViewState>) {

                    assertEquals(
                        value.data?.noteList,
                        dummyListOfNotes
                    )
                    assertEquals(
                        value.stateMessage?.response?.message,
                        SearchNotes.SEARCH_NOTES_NO_MATCHING_RESULTS
                    )
                }

            })
    }



    // 3) Returns stateMessage=ERROR due to exception
    @Test
    fun searchNotes_throwException_showErrorMessage() = runBlocking {
        val noteRepository = mockk<NoteRepository>()
        every {
            runBlocking {
                noteRepository.searchNotes(any(), any(), any())
            }
        } throws Exception("Something went wrong")

        searchNotes = SearchNotes(noteRepository)
        (searchNotes as SearchNotes).searchNotes("", "", 0, SearchNotesEvent())
            .collect(object: FlowCollector<DataState<NoteListViewState>>{
                override suspend fun emit(value: DataState<NoteListViewState>) {

                    assertThat(
                        value.stateMessage?.response?.message,
                        containsString(SearchNotesEvent().errorInfo())
                    )
                }
            })
    }


}
















