package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state

import com.codingwithmitch.cleannotes.core.business.state.StateEvent
import com.codingwithmitch.cleannotes.core.business.state.StateMessage
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note

sealed class NoteListStateEvent: StateEvent {

    class InsertNewNoteEvent(
        val title: String,
        val body: String
    ): NoteListStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting new note."
        }

        override fun eventName(): String {
            return "InsertNewNoteEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class DeleteNoteEvent(
        val primaryKey: Int
    ): NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error deleting note."
        }

        override fun eventName(): String {
            return "DeleteNoteEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class DeleteMultipleNotesEvent(
        val primaryKeys: IntArray
    ): NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error deleting the selected notes."
        }

        override fun eventName(): String {
            return "DeleteMultipleNotesEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class RestoreDeletedNoteEvent(
        val note: Note
    ): NoteListStateEvent() {

        override fun errorInfo(): String {
            return "Error restoring the note that was deleted."
        }

        override fun eventName(): String {
            return "RestoreDeletedNoteEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class SearchNotesEvent(
        val clearLayoutManagerState: Boolean = true
    ): NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error getting list of notes."
        }

        override fun eventName(): String {
            return "SearchNotesEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetNumNotesInCacheEvent: NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error getting the number of notes from the cache."
        }

        override fun eventName(): String {
            return "GetNumNotesInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















