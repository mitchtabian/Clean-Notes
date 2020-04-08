package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state

import com.codingwithmitch.cleannotes.core.business.state.StateEvent
import com.codingwithmitch.cleannotes.core.business.state.StateMessage

sealed class NoteListStateEvent: StateEvent {

    class InsertNewNoteEvent(
        val title: String,
        val body: String
    ): NoteListStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting new note."
        }

        override fun EventName(): String {
            return "InsertNewNoteEvent"
        }
    }

    class DeleteNoteEvent(
        val primaryKey: Int
    ): NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error deleting note."
        }

        override fun EventName(): String {
            return "DeleteNoteEvent"
        }
    }

    class SearchNotesEvent(
        val clearLayoutManagerState: Boolean = true
    ): NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error getting list of notes."
        }

        override fun EventName(): String {
            return "GetNotesEvent"
        }
    }

    class GetNumNotesInCacheEvent: NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error getting the number of notes from the cache."
        }

        override fun EventName(): String {
            return "GetNumNotesInCacheEvent"
        }
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): NoteListStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun EventName(): String {
            return "GetNotesEvent"
        }
    }

}




















