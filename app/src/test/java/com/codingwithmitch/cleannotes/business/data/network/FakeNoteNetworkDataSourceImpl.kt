package com.codingwithmitch.cleannotes.business.data.network

import com.codingwithmitch.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note

class FakeNoteNetworkDataSourceImpl
constructor(
    private val notesData: HashMap<String, Note>,
    private val deletedNotesData: HashMap<String, Note>
) : NoteNetworkDataSource{

    override suspend fun insertOrUpdateNote(note: Note) {
        notesData.put(note.id, note)
    }

    override suspend fun deleteNote(primaryKey: String) {
        notesData.remove(primaryKey)
    }

    override suspend fun insertDeletedNote(note: Note) {
        deletedNotesData.put(note.id, note)
    }

    override suspend fun insertDeletedNotes(notes: List<Note>) {
        for(note in notes){
            deletedNotesData.put(note.id, note)
        }
    }

    override suspend fun deleteDeletedNote(note: Note) {
        deletedNotesData.remove(note.id)
    }

    override suspend fun getDeletedNotes(): List<Note> {
        return ArrayList(deletedNotesData.values)
    }

    override suspend fun deleteAllNotes() {
        deletedNotesData.clear()
    }

    override suspend fun searchNote(note: Note): Note? {
        return notesData.get(note.id)
    }

    override suspend fun getAllNotes(): List<Note> {
        return ArrayList(notesData.values)
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>) {
        for(note in notes){
            notesData.put(note.id, note)
        }
    }
}
