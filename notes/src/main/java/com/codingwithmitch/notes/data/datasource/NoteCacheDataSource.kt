package com.codingwithmitch.notes.data.datasource

import com.codingwithmitch.notes.domain.model.Note

interface NoteCacheDataSource{

    suspend fun insert(note: Note): Long

    suspend fun delete(note: Note): Int

    suspend fun update(note: Note): Int

    suspend fun get(): List<Note>

}
