package com.codingwithmitch.cleannotes.business.interactors.network_sync

import com.codingwithmitch.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource

/*
    Search firestore for all notes in the "deleted" node.
    It will then search the cache for notes matching those deleted notes.
    If a match is found, it is deleted from the cache.
 */
class SyncDeletedNotes(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
){

    suspend fun syncDeletedNotes(){

    }

}
























