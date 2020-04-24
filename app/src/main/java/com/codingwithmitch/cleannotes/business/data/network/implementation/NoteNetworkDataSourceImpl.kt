package com.codingwithmitch.cleannotes.business.data.network.implementation

import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.framework.datasource.network.abstraction.NoteFirestoreService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Singleton


@Singleton
class NoteNetworkDataSourceImpl
constructor(
    private val firestoreService: NoteFirestoreService
): NoteNetworkDataSource {

    override suspend fun insertOrUpdateNote(note: Note): Task<Void> {
        return firestoreService.insertOrUpdateNote(note)
    }

    override suspend fun deleteNote(primaryKey: String): Task<Void> {
        return firestoreService.deleteNote(primaryKey)
    }

    override suspend fun insertDeletedNote(note: Note): Task<Void> {
        return firestoreService.insertDeletedNote(note)
    }

    override suspend fun insertDeletedNotes(notes: List<Note>): List<Task<Void>> {
        return firestoreService.insertDeletedNotes(notes)
    }

    override suspend fun findUpdatedNotes(previousSyncTimestamp: Long): Task<QuerySnapshot> {
        return firestoreService.findUpdatedNotes(previousSyncTimestamp)
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): List<Task<Void>> {
        return firestoreService.insertOrUpdateNotes(notes)
    }


}





























