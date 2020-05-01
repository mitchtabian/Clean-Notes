package com.codingwithmitch.cleannotes.business.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    val id: String,
    val title: String,
    val body: String,
    val updated_at: String,
    val created_at: String
) : Parcelable{

    // no arg constructor for mapping jackson
    constructor(): this(
        "",
        "",
        "",
        "",
        ""
    )
}