package com.codingwithmitch.cleannotes.framework.datasource.network.model

import com.google.firebase.Timestamp


data class NoteNetworkEntity(

    var id: String,

    var title: String,

    var body: String,

    var updated_at: Timestamp,

    var created_at: Timestamp

){

    // no arg constructor for firestore
    constructor(): this(
        "",
        "",
        "",
        Timestamp.now(),
        Timestamp.now()
    )



    companion object{

        const val UPDATED_AT_FIELD = "updated_at"
        const val TITLE_FIELD = "title"
        const val BODY_FIELD = "body"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteNetworkEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (body != other.body) return false
        if (created_at != other.created_at) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + updated_at.hashCode()
        result = 31 * result + created_at.hashCode()
        return result
    }
}