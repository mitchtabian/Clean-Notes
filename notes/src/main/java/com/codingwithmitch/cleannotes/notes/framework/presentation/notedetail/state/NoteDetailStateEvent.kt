package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state

import com.codingwithmitch.cleannotes.core.business.state.StateEvent
import com.codingwithmitch.cleannotes.core.business.state.StateMessage

sealed class NoteDetailStateEvent: StateEvent {


    class DeleteNoteEvent(
        val primaryKey: Int
    ): NoteDetailStateEvent(){

        override fun errorInfo(): String {
            return "Error deleting note."
        }

        override fun EventName(): String {
            return "DeleteNoteEvent"
        }
    }


    class UpdateNoteEvent: NoteDetailStateEvent(){

        override fun errorInfo(): String {
            return "Error updating note."
        }

        override fun EventName(): String {
            return "UpdateNoteEvent"
        }
    }


    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): NoteDetailStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun EventName(): String {
            return "GetNotesEvent"
        }
    }

}




















