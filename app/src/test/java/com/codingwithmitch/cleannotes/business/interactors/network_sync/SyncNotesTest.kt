package com.codingwithmitch.cleannotes.business.interactors.network_sync

import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.di.DependencyContainer
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.ORDER_BY_ASC_DATE_UPDATED
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList


/*

Test cases:
1. insertNetworkNotesIntoCache()
    a) insert a bunch of new notes into the cache
    b) perform the sync
    c) check to see that those notes were inserted into the network
2. insertCachedNotesIntoNetwork()
    a) insert a bunch of new notes into the network
    b) perform the sync
    c) check to see that those notes were inserted into the cache
3. checkCacheUpdateLogicSync()
    a) select some notes from the cache and update them
    b) perform sync
    c) confirm network reflects the updates
4. checkNetworkUpdateLogicSync()
    a) select some notes from the network and update them
    b) perform sync
    c) confirm cache reflects the updates

 */

@InternalCoroutinesApi
class SyncNotesTest {

    // system in test
    private val syncNotes: SyncNotes

    // dependencies
    private val dependencyContainer: DependencyContainer
    private val noteCacheDataSource: NoteCacheDataSource
    private val noteNetworkDataSource: NoteNetworkDataSource
    private val noteFactory: NoteFactory
    private val dateUtil: DateUtil

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        noteCacheDataSource = dependencyContainer.noteCacheDataSource
        noteNetworkDataSource = dependencyContainer.noteNetworkDataSource
        noteFactory = dependencyContainer.noteFactory
        dateUtil = dependencyContainer.dateUtil
        syncNotes = SyncNotes(
            noteCacheDataSource = noteCacheDataSource,
            noteNetworkDataSource = noteNetworkDataSource,
            dateUtil = dateUtil
        )
    }

    @Test
    fun insertNetworkNotesIntoCache() = runBlocking {

        // prepare the scenario
        // -> Notes in network are newer so they must be inserted into cache
        val newNotes = noteFactory.createNoteList(50)
        noteNetworkDataSource.insertOrUpdateNotes(newNotes)

        // perform the sync
        syncNotes.syncNotes()

        // confirm the new notes were inserted into cache
        for(note in newNotes){
            val cachedNote = noteCacheDataSource.searchNoteById(note.id)
            assertTrue { cachedNote != null }
        }
    }


    @Test
    fun insertCachedNotesIntoNetwork() = runBlocking {

        // prepare the scenario
        // -> Notes in cache are newer so they must be inserted into network
        val newNotes = noteFactory.createNoteList(50)
        noteCacheDataSource.insertNotes(newNotes)

        // perform the sync
        syncNotes.syncNotes()

        // confirm the new notes were inserted into network
        for(note in newNotes){
            val networkNote = noteNetworkDataSource.searchNote(note)
            assertTrue { networkNote != null }
        }
    }

    @Test
    fun checkCacheUpdateLogicSync() = runBlocking {

        // select a few notes from cache and update the title and body
        val cachedNotes = noteCacheDataSource.searchNotes(
            query = "",
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
            page = 1
        )
        val notesToUpdate: ArrayList<Note> = ArrayList()
        for(note in cachedNotes){
            val updatedNote = noteFactory.createSingleNote(
                id = note.id,
                title = UUID.randomUUID().toString(),
                body = UUID.randomUUID().toString()
            )
            notesToUpdate.add(updatedNote)
            if(notesToUpdate.size > 3){
                break
            }
        }
        noteCacheDataSource.insertNotes(notesToUpdate)

        // perform sync
        syncNotes.syncNotes()

        // confirm the updated notes were updated in the network
        for(note in notesToUpdate){
            val networkNote = noteNetworkDataSource.searchNote(note)
            assertEquals(note.id, networkNote?.id)
            assertEquals(note.title, networkNote?.title)
            assertEquals(note.body, networkNote?.body)
            assertEquals(note.updated_at, networkNote?.updated_at)
        }
    }

    @Test
    fun checkNetworkUpdateLogicSync() = runBlocking {

        // select a few notes from network and update the title and body
        val networkNotes = noteNetworkDataSource.getAllNotes()

        val notesToUpdate: ArrayList<Note> = ArrayList()
        for(note in networkNotes){
            val updatedNote = noteFactory.createSingleNote(
                id = note.id,
                title = UUID.randomUUID().toString(),
                body = UUID.randomUUID().toString()
            )
            notesToUpdate.add(updatedNote)
            if(notesToUpdate.size > 3){
                break
            }
        }
        noteNetworkDataSource.insertOrUpdateNotes(notesToUpdate)

        // perform sync
        syncNotes.syncNotes()

        // confirm the updated notes were updated in the cache
        for(note in notesToUpdate){
            val cacheNote = noteCacheDataSource.searchNoteById(note.id)
            assertEquals(note.id, cacheNote?.id)
            assertEquals(note.title, cacheNote?.title)
            assertEquals(note.body, cacheNote?.body)
            assertEquals(note.updated_at, cacheNote?.updated_at)
        }
    }
}







































