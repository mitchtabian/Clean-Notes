package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state

import android.os.Parcelable
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteListViewState(

    var noteList: List<Note>? = null,
    var newNote: Note? = null, // note that can be created with fab
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null,
    var layoutManagerState: Parcelable? = null,
    var numNotesInCache: Int? = null

) : Parcelable
























