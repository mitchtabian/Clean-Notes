package com.codingwithmitch.cleannotes.notes

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.notes.business.data.repository.NoteRepositoryImpl
import com.codingwithmitch.cleannotes.notes.framework.datasource.mappers.NoteEntityMapper
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.domain.repository.NoteRepository
import com.codingwithmitch.cleannotes.notes.business.interactors.*
import com.codingwithmitch.cleannotes.core.business.DateUtil
import com.codingwithmitch.notes.datasource.cache.db.NoteDatabase
import com.codingwithmitch.notes.datasource.cache.repository.NoteCacheDataSourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.assertEquals

/**
 * BUG WITH TESTING DYNAMIC FEATURES:
 * https://issuetracker.google.com/issues/145191501
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class BasicDbTests {


    private val dateUtil: DateUtil =
        DateUtil()
    private lateinit var db: NoteDatabase
    private lateinit var noteRepository: NoteRepository
    private lateinit var interactors: NoteListInteractors

    @Before
    fun setupInteractors() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, NoteDatabase::class.java).build()
        noteRepository = NoteRepositoryImpl(
            NoteCacheDataSourceImpl(
                noteDao = db.noteDao(),
                noteEntityMapper = NoteEntityMapper(dateUtil),
                dateUtil = dateUtil
            )
        )
        interactors = NoteListInteractors(
            InsertNewNote(noteRepository),
            DeleteNote(noteRepository),
            UpdateNote(noteRepository),
            GetNotes(noteRepository)
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeNote_readList_updateNote_deleteNote() {

        val newTitle = "first note"
        val newBody = "something I should not forget!"
        runBlocking {

            interactors
                .insertNewNote
                .insertNewNote(
                    title = newTitle,
                    body = newBody
                )
        }
        var notes: List<Note>? = null
        runBlocking {
            notes = interactors.getNotes.getNotes()
        }
        assertEquals(newTitle, notes?.get(0)?.title)
        assertEquals(newBody, notes?.get(0)?.body)

        val updatedTitle = "new title baby"
        val updatedBody = "and some new content"

        runBlocking {
            interactors
                .updateNote
                .updateNote(
                    notes!!.get(0),
                    updatedTitle,
                    updatedBody
                )
            notes = interactors.getNotes.getNotes()
        }
        assertEquals(updatedTitle, notes?.get(0)?.title)
        assertEquals(updatedBody, notes?.get(0)?.body)

        runBlocking {
            interactors
                .deleteNote
                .deleteNote(notes!!.get(0).id)
            notes = interactors.getNotes.getNotes()
        }
        assertEquals(notes?.size, 0)


    }
}















