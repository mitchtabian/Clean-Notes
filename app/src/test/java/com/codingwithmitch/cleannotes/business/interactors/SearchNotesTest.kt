package com.codingwithmitch.cleannotes.business.interactors

import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.interactors.notelist.SearchNotes
import com.codingwithmitch.cleannotes.business.state.DataState
import com.codingwithmitch.cleannotes.business.data.NoteDataFactory
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

@InternalCoroutinesApi
class SearchNotesTest {

    // system in test
    private var searchNotes: SearchNotes? = null

    private lateinit var noteFactory: NoteDataFactory

    init {
        this.javaClass.classLoader?.let { classLoader ->
            noteFactory =
                NoteDataFactory(
                    classLoader
                )
        }
    }

    @AfterEach
    fun after(){
        searchNotes = null
    }

    // 1) Returns data=List<Note>(non-empty) and message=SEARCH_NOTES_SUCCESS
    @Test
    fun searchNotes_returnList_messageSuccess() = runBlocking {
        val noteCacheDataSource = mockk<NoteCacheDataSource>()
        val dummyListOfNotes = noteFactory.produceListOfNotes()
        every {
            runBlocking {
                noteCacheDataSource.searchNotes(any(), any(), any())
            }
        } returns dummyListOfNotes

        searchNotes = SearchNotes(noteCacheDataSource)
        (searchNotes as SearchNotes).searchNotes("", "", 0, SearchNotesEvent())
            .collect(object: FlowCollector<DataState<NoteListViewState>?>{
                override suspend fun emit(value: DataState<NoteListViewState>?) {

                    assertEquals(
                        value?.data?.noteList,
                        dummyListOfNotes
                    )
                    assertEquals(
                        value?.stateMessage?.response?.message,
                        SearchNotes.SEARCH_NOTES_SUCCESS
                    )
                }

            })
    }

    // 2) Returns data=List<Note>(empty) and message=SEARCH_NOTES_NO_MATCHING_RESULTS
    @Test
    fun searchNotes_returnEmptyList_messageSuccess() = runBlocking {
        val noteCacheDataSource = mockk<NoteCacheDataSource>()
        val dummyListOfNotes = noteFactory.produceEmptyListOfNotes()
        every {
            runBlocking {
                noteCacheDataSource.searchNotes(any(), any(), any())
            }
        } returns dummyListOfNotes

        searchNotes = SearchNotes(noteCacheDataSource)
        (searchNotes as SearchNotes).searchNotes("", "", 0, SearchNotesEvent())
            .collect(object: FlowCollector<DataState<NoteListViewState>?>{
                override suspend fun emit(value: DataState<NoteListViewState>?) {

                    assertEquals(
                        value?.data?.noteList,
                        dummyListOfNotes
                    )
                    assertEquals(
                        value?.stateMessage?.response?.message,
                        SearchNotes.SEARCH_NOTES_NO_MATCHING_RESULTS
                    )
                }

            })
    }



    // 3) Returns stateMessage=ERROR due to exception
    @Test
    fun searchNotes_throwException_showErrorMessage() = runBlocking {
        val noteCacheDataSource = mockk<NoteCacheDataSource>()
        every {
            runBlocking {
                noteCacheDataSource.searchNotes(any(), any(), any())
            }
        } throws Exception("Something went wrong")

        searchNotes = SearchNotes(noteCacheDataSource)
        (searchNotes as SearchNotes).searchNotes("", "", 0, SearchNotesEvent())
            .collect(object: FlowCollector<DataState<NoteListViewState>?>{
                override suspend fun emit(value: DataState<NoteListViewState>?) {

                    assertThat(
                        value?.stateMessage?.response?.message,
                        containsString(SearchNotesEvent().errorInfo())
                    )
                }
            })
    }


}
















