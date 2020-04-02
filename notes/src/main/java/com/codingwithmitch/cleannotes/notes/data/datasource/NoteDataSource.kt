package com.codingwithmitch.cleannotes.notes.data.datasource

import com.codingwithmitch.cleannotes.notes.domain.model.Note

interface NoteCacheDataSource{

    suspend fun insert(note: Note): Long

    suspend fun delete(note: Note): Int

    suspend fun update(note: Note): Int

    suspend fun get(): List<Note>
}

interface NoteNetworkDataSource{

    suspend fun insert(note: Note): Long

    suspend fun delete(note: Note): Int

    suspend fun update(note: Note): Int

    suspend fun get(): List<Note>

}





