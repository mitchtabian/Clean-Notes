package com.codingwithmitch.cleannotes.notes.framework.presentation.state

import com.codingwithmitch.cleannotes.core.business.state.StateEvent
import com.codingwithmitch.cleannotes.core.business.state.StateMessage
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note

sealed class NoteStateEvent: StateEvent {

    class InsertNewNoteEvent(
        val title: String,
        val body: String
    ): NoteStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting new note."
        }

        override fun EventName(): String {
            return "InsertNewNoteEvent"
        }
    }

    class DeleteNoteEvent(
        val primaryKey: Int
    ): NoteStateEvent(){

        override fun errorInfo(): String {
            return "Error deleting note."
        }

        override fun EventName(): String {
            return "DeleteNoteEvent"
        }
    }


    class UpdateNoteEvent(
        val newTitle: String,
        val newBody: String?
    ): NoteStateEvent(){

        override fun errorInfo(): String {
            return "Error updating note."
        }

        override fun EventName(): String {
            return "UpdateNoteEvent"
        }
    }


    class SearchNotesEvent(
        val clearLayoutManagerState: Boolean = true
    ): NoteStateEvent(){

        override fun errorInfo(): String {
            return "Error getting list of notes."
        }

        override fun EventName(): String {
            return "GetNotesEvent"
        }
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): NoteStateEvent(){

        override fun errorInfo(): String {
            return "Error getting list of notes."
        }

        override fun EventName(): String {
            return "GetNotesEvent"
        }
    }

}




















