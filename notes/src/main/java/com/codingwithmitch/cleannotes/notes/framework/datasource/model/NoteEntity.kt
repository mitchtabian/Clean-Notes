package com.codingwithmitch.cleannotes.notes.framework.datasource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "body")
    var body: String,

    @ColumnInfo(name = "updated_at")
    var updated_at: Long,

    @ColumnInfo(name = "created_at")
    var created_at: Long

){

    companion object{

        fun nullIdError(): String{
            return "NoteEntity object has a null id. This should not be possible. Check local database."
        }
    }
}



