package com.codingwithmitch.cleannotes.business.data.abstraction

import com.codingwithmitch.cleannotes.business.domain.model.Note


interface NoteNetworkDataSource{

    suspend fun insert(note: Note): Long

    suspend fun delete(note: Note): Int

    suspend fun update(note: Note): Int

    suspend fun get(): List<Note>

}
