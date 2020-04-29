package com.codingwithmitch.cleannotes.framework.datasource.cache

import com.codingwithmitch.cleannotes.BaseTest
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.di.TestAppComponent
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.mappers.CacheMapper
import com.codingwithmitch.cleannotes.framework.datasource.data.NoteDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
open class CacheTest: BaseTest() {

    @Inject
    lateinit var dao: NoteDao

    @Inject
    lateinit var noteDataFactory: NoteDataFactory

    @Inject
    lateinit var dateUtil: DateUtil

    @Inject
    lateinit var noteFactory: NoteFactory

    @Inject
    lateinit var cacheMapper: CacheMapper

    init {
        injectTest()
        insertTestData()
    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

    fun insertTestData() = runBlocking{
        val entityList = cacheMapper.noteListToEntityList(
            noteDataFactory.produceListOfNotes()
        )
        for(entity in entityList){
            dao.insertNotes(entityList)
        }
    }


}












