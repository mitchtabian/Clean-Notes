package com.codingwithmitch.cleannotes.framework.datasource.network.abstraction

import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface NoteFirestoreService {

    suspend fun insertOrUpdateNote(note: Note): Task<Void>

    suspend fun deleteNote(primaryKey: String): Task<Void>

    suspend fun insertDeletedNote(note: Note): Task<Void>

    suspend fun insertDeletedNotes(notes: List<Note>): List<Task<Void>>

    suspend fun deleteDeletedNote(note: Note): Task<Void>

    suspend fun searchNote(note: Note): Task<DocumentSnapshot>

    suspend fun getAllNotes(): Task<QuerySnapshot>

    suspend fun insertOrUpdateNotes(notes: List<Note>): List<Task<Void>>


}