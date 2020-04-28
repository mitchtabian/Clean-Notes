package com.codingwithmitch.cleannotes.business.data.network.implementation

import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.framework.datasource.network.abstraction.NoteFirestoreService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
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

    override suspend fun insertDeletedNotes(notes: List<Note>): Task<Void> {
        return firestoreService.insertDeletedNotes(notes)
    }

    override suspend fun deleteDeletedNote(note: Note): Task<Void> {
        return firestoreService.deleteDeletedNote(note)
    }

    override suspend fun getDeletedNotes(): Task<QuerySnapshot> {
        return firestoreService.getDeletedNotes()
    }

    override suspend fun searchNote(note: Note): Task<DocumentSnapshot> {
        return firestoreService.searchNote(note)
    }

    override suspend fun getAllNotes(): Task<QuerySnapshot> {
        return firestoreService.getAllNotes()
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): Task<Void> {
        return firestoreService.insertOrUpdateNotes(notes)
    }


}





























