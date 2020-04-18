package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state

sealed class NoteListToolbarState {

    class MultiSelectionState: NoteListToolbarState(){

        override fun toString(): String {
            return "MultiSelectionState"
        }
    }

    class SearchViewState: NoteListToolbarState(){

        override fun toString(): String {
            return "SearchViewState"
        }
    }
}