package com.codingwithmitch.cleannotes.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingwithmitch.cleannotes.framework.datasource.cache.abstraction.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.model.NoteEntity

@Database(entities = [NoteEntity::class ], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object{
        val DATABASE_NAME: String = "note_db"
    }


}