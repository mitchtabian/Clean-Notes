package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state

import android.os.Parcelable
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteDetailViewState(

    var note: Note? = null

) : Parcelable




















