package com.codingwithmitch.cleannotes.business.data.implementation

import com.codingwithmitch.cleannotes.business.data.abstraction.NoteNetworkDataSource
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

    override suspend fun insertNote(note: Note): Task<Void> {
        return firestoreService.insertNote(note)
    }

    override suspend fun deleteNote(primaryKey: String): Task<Void> {
        return firestoreService.deleteNote(primaryKey)
    }

    override suspend fun updateNote(
        primaryKey: String,
        newTitle: String,
        newBody: String?
    ): Task<Void> {
        return firestoreService.updateNote(
            primaryKey,
            newTitle,
            newBody
        )
    }

    override suspend fun findUpdatedNotes(previousSyncTimestamp: Long): Task<QuerySnapshot> {
        return firestoreService.findUpdatedNotes(previousSyncTimestamp)
    }

    override suspend fun insertNotes(notes: List<Note>): List<Task<Void>> {
        return firestoreService.insertNotes(notes)
    }


}





























