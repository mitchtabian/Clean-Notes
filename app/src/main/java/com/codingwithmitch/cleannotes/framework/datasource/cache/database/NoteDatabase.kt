package com.codingwithmitch.cleannotes.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingwithmitch.cleannotes.framework.datasource.cache.model.NoteCacheEntity

@Database(entities = [NoteCacheEntity::class ], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object{
        val DATABASE_NAME: String = "note_db"
    }


}