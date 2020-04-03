package com.codingwithmitch.cleannotes.notes.framework.presentation.state

import android.os.Parcelable
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteViewState(

    var noteListViewState: NoteListViewState = NoteListViewState(),

    var noteDetailViewState: NoteDetailViewState = NoteDetailViewState()

) : Parcelable {

    @Parcelize
    data class NoteListViewState(
        var noteList: List<Note>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ): Parcelable


    @Parcelize
    data class NoteDetailViewState(
        var note: Note? = null
    ) : Parcelable
}




















