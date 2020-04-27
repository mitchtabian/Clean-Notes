package com.codingwithmitch.cleannotes.business.interactors.network_sync

import com.codingwithmitch.cleannotes.business.data.cache.CacheResponseHandler
import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.state.DataState
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.business.util.safeCacheCall
import com.codingwithmitch.cleannotes.framework.datasource.network.mappers.NetworkMapper
import com.codingwithmitch.cleannotes.framework.datasource.network.model.NoteNetworkEntity
import com.codingwithmitch.cleannotes.util.printLogD
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await

/*
    Query all notes in the cache. It will then search firestore for
    each corresponding note but with an extra filter: It will only return notes where
    cached_note.updated_at < network_note.updated_at. It will update the cached notes
    where that condition is met. If the note does not exist in Firestore (maybe due to
    network being down at time of insertion), insert it (**This must be done AFTER
    checking for deleted notes and performing that sync**).
 */
@Suppress("IMPLICIT_CAST_TO_ANY")
class SyncNotes(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource,
    private val dateUtil: DateUtil,
    private val networkMapper: NetworkMapper
){

    suspend fun syncNotes() {

        val cachedNotesList = getCachedNotes()

        printLogD("SyncNotes", "notelist: ${cachedNotesList.size}")
        syncNetworkNotesWithCachedNotes(ArrayList(cachedNotesList))
    }

    private suspend fun getCachedNotes(): List<Note> {
        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.searchNotes("", "", 1)
        }

        val response = object: CacheResponseHandler<List<Note>, List<Note>>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Note>): DataState<List<Note>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }

        }.getResult()

        return response?.data ?: ArrayList()
    }

    // get all notes from network
    // if they do not exist in cache, insert them
    // if they do exist in cache, make sure they are up to date
    // while looping, remove notes from the cachedNotes list. If any remain, it means they
    // should be in the network but aren't. So insert them.
    private suspend fun syncNetworkNotesWithCachedNotes(
        cachedNotes: ArrayList<Note>
    ) = withContext(IO){

        val querySnapshot = noteNetworkDataSource.getAllNotes().await()
        val noteList = querySnapshot.toObjects(NoteNetworkEntity::class.java)

        val job = launch {
            for(note in noteList){
                noteCacheDataSource.searchNoteById(note.id)?.let { cachedNote ->
                    cachedNotes.remove(cachedNote)
                    checkIfCachedNoteRequiresUpdate(cachedNote, note)
                }?: noteCacheDataSource.insertNote(networkMapper.mapFromEntity(note))
            }
        }
        job.join()

        // insert remaining into network
        for(cachedNote in cachedNotes){
            noteNetworkDataSource.insertOrUpdateNote(cachedNote)
        }
    }

    private suspend fun checkIfCachedNoteRequiresUpdate(
        cachedNote: Note,
        networkNote: NoteNetworkEntity
    ){
        val cacheUpdatedAt = dateUtil.convertServerStringDateToLong(cachedNote.updated_at) / 1000
        val networkUpdatedAt = networkNote.updated_at.toDate().time / 1000
        if(networkUpdatedAt > cacheUpdatedAt){
            printLogD("SyncNotes",
                "cacheUpdatedAt: ${cacheUpdatedAt}, " +
                        "networkUpdatedAt: ${networkUpdatedAt}, " +
                        "note: ${cachedNote.title}")
            safeCacheCall(IO){
                noteCacheDataSource.updateNote(
                    networkNote.id,
                    networkNote.title,
                    networkNote.body
                )
            }
        }
    }

    // for debugging
//    private fun printCacheLongTimestamps(notes: List<Note>){
//        for(note in notes){
//            printLogD("SyncNotes",
//                "date: ${dateUtil.convertServerStringDateToLong(note.updated_at)}")
//        }
//    }

}









































