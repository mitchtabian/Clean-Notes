package com.codingwithmitch.cleannotes.notes.framework.presentation.state



sealed class NoteInteractionState {

    class EditState: NoteInteractionState() {

        override fun toString(): String {
            return "EditState"
        }
    }

    // EditText's will not be editable
    class DefaultState: NoteInteractionState(){

        override fun toString(): String {
            return "DefaultState"
        }
    }
}