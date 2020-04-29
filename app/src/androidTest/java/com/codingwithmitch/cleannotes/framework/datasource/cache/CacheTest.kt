package com.codingwithmitch.cleannotes.framework.datasource.cache

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDatabase
import com.codingwithmitch.cleannotes.framework.datasource.cache.mappers.CacheMapper
import com.codingwithmitch.cleannotes.framework.datasource.data.NoteDataFactory
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

open class CacheTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val dao: NoteDao
    private val db: NoteDatabase
    private val noteDataFactory: NoteDataFactory

    val dateUtil: DateUtil = DateUtil(dateFormat)
    val noteFactory = NoteFactory(dateUtil)
    val cacheMapper = CacheMapper(dateUtil)

    init {
        val application = ApplicationProvider.getApplicationContext<Context>() as Application
        noteDataFactory = NoteDataFactory(application)
        db = Room.inMemoryDatabaseBuilder(
            application,
            NoteDatabase::class.java
        ).build()
        dao = db.noteDao()
        insertTestData()
    }

    fun insertTestData() = runBlocking{
        val entityList = cacheMapper.noteListToEntityList(
            noteDataFactory.produceListOfNotes()
        )
        for(entity in entityList){
            getDao().insertNotes(entityList)
        }
    }

    fun getDb(): NoteDatabase{
        return db
    }

    fun getDao(): NoteDao{
        return dao
    }

}












