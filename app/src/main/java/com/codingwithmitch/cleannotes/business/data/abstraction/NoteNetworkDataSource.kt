package com.codingwithmitch.cleannotes.business.data.abstraction

import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot


interface NoteNetworkDataSource{

    suspend fun insertNote(note: Note): Task<Void>

    suspend fun deleteNote(primaryKey: String): Task<Void>

    suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String?): Task<Void>

    suspend fun findUpdatedNotes(previousSyncTimestamp: Long): Task<QuerySnapshot>

    suspend fun insertNotes(notes: List<Note>): List<Task<Void>>

}
