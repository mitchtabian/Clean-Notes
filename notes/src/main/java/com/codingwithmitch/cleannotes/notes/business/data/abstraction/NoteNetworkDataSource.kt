package com.codingwithmitch.cleannotes.notes.business.data.abstraction

import com.codingwithmitch.cleannotes.notes.business.domain.model.Note

interface NoteNetworkDataSource{

    suspend fun insert(note: Note): Long

    suspend fun delete(note: Note): Int

    suspend fun update(note: Note): Int

    suspend fun get(): List<Note>

}
