package com.codingwithmitch.cleannotes.framework.datasource.network.model

import com.google.firebase.Timestamp


data class NoteNetworkEntity(

    var id: String,

    var title: String,

    var body: String,

    var updated_at: Timestamp,

    var created_at: Timestamp

){

    companion object{

        const val UPDATED_AT_FIELD = "updated_at"
        const val TITLE_FIELD = "title"
        const val BODY_FIELD = "body"
    }
}









