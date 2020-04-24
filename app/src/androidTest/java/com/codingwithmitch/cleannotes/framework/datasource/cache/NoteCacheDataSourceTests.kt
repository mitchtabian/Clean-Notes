package com.codingwithmitch.cleannotes.framework.datasource.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NOTE_FILTER_DATE_CREATED
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDatabase
import com.codingwithmitch.cleannotes.business.data.cache.implementation.NoteCacheDataSourceImpl
import com.codingwithmitch.cleannotes.framework.datasource.cache.abstraction.NoteDaoService
import com.codingwithmitch.cleannotes.framework.datasource.cache.implementation.NoteDaoServiceImpl
import com.codingwithmitch.cleannotes.framework.datasource.cache.mappers.CacheMapper
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals


// runBlockingTest doesn't work:
// https://github.com/Kotlin/kotlinx.coroutines/issues/1204
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NoteCacheDataSourceTests {

    // system in test
    private lateinit var dataSource: NoteCacheDataSource

    private lateinit var noteDaoService: NoteDaoService
    private lateinit var dao: NoteDao
    private lateinit var db: NoteDatabase
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val dateUtil: DateUtil = DateUtil(dateFormat)
    private val noteFactory = NoteFactory(dateUtil)
    private val mapper = CacheMapper(dateUtil)

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NoteDatabase::class.java
        ).build()
        dao = db.noteDao()
        noteDaoService = NoteDaoServiceImpl(
            noteDao = dao,
            noteMapper = mapper,
            dateUtil = dateUtil
        )
        dataSource = NoteCacheDataSourceImpl(noteDaoService)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertNote_savesData()= runBlocking {

        val newNote = noteFactory.createSingleNote(
            null,
            "Super cool title",
            "Some content for the note"
            )
        dataSource.insertNote(newNote)

        val notes = dataSource.searchNotes(
            "",
            NOTE_FILTER_DATE_CREATED,
            1
        )
        assert(notes.contains(newNote))
    }

    @Test
    fun searchNotes_returnList() = runBlocking {

        val noteList = noteFactory.createNoteList(10)
        dataSource.insertNotes(noteList)

        val queriedNotes = dataSource.searchNotes(
            "",
            NOTE_FILTER_DATE_CREATED,
            1
        )
        for((index,note) in queriedNotes.withIndex()){
            assertEquals(noteList.get(index).id, note.id)
            assertEquals(noteList.get(index).title, note.title)
            assertEquals(noteList.get(index).body, note.body)
            assertEquals(
                noteList.get(index).updated_at,
                note.updated_at
            )
            assertEquals(
                noteList.get(index).created_at,
                note.created_at
            )
        }
    }

    @Test
    fun insert1000Notes_searchNotesByTitle_confirm50ExpectedValues() = runBlocking {

        // insert 1000 notes
        val noteList = noteFactory.createNoteList(1000)
        dataSource.insertNotes(noteList)

        // query 50 notes by specific title
        repeat(50){
            val randomIndex = Random.nextInt(0, noteList.size - 1)
            val result = dao.searchNotesOrderByTitleASC(
                query = noteList.get(randomIndex).title,
                page = 1,
                pageSize = 1
            )
            println("result: size: ${result.size}")
            println("result: ${randomIndex}: " +
                    "expected: ${noteList.get(randomIndex).title}, " +
                    "actual: ${result.get(0).title}")
            assertEquals(noteList.get(randomIndex).title, result.get(0).title)
        }
    }

    @Test
    fun insertNote_deleteNote_confirmDeleted() = runBlocking {
        val newNote = noteFactory.createSingleNote(
            null,
            "Super cool title",
            "Some content for the note"
        )
        dataSource.insertNote(newNote)

        var notes = dataSource.searchNotes(
            "",
            NOTE_FILTER_DATE_CREATED,
            1
        )
        assert(notes.contains(newNote))

        dataSource.deleteNote(newNote.id)
        notes = dataSource.searchNotes(
            "",
            NOTE_FILTER_DATE_CREATED,
            1
        )
        assert(!notes.contains(newNote))
    }

    @Test
    fun insertNote_updateNote_confirmUpdated() = runBlocking {
        val newNote = noteFactory.createSingleNote(
            null,
            "Super cool title",
            "Some content for the note"
        )
        dataSource.insertNote(newNote)

        val newTitle = UUID.randomUUID().toString()
        val newBody = UUID.randomUUID().toString()
        dataSource.updateNote(
            primaryKey = newNote.id,
            newTitle = newTitle,
            newBody = newBody
        )

        val updatedNote = dataSource.searchNotes(
            "",
            NOTE_FILTER_DATE_CREATED,
            1
        ).get(0)
        assertEquals(newNote.id, updatedNote.id)
        assertEquals(newTitle, updatedNote.title)
        assertEquals(newBody, updatedNote.body)
        assert(newNote.updated_at != updatedNote.updated_at)
        assertEquals(
            newNote.created_at,
            updatedNote.created_at
        )
    }

    @Test
    fun insert1000Notes_confirmNumNotesInDb() = runBlocking {
        // insert 1000 notes
        val noteList = noteFactory.createNoteList(1000)
        dataSource.insertNotes(noteList)

        val numNotes = dataSource.getNumNotes()
        assertEquals(1000, numNotes)
    }

}














