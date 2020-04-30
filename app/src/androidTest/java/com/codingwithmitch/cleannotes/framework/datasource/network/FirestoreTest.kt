package com.codingwithmitch.cleannotes.framework.datasource.network

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.codingwithmitch.cleannotes.BaseTest
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.di.TestAppComponent
import com.codingwithmitch.cleannotes.framework.datasource.data.NoteDataFactory
import com.codingwithmitch.cleannotes.framework.datasource.network.implementation.NoteFirestoreServiceImpl
import com.codingwithmitch.cleannotes.framework.datasource.network.implementation.NoteFirestoreServiceImpl.Companion.NOTES_COLLECTION
import com.codingwithmitch.cleannotes.framework.datasource.network.implementation.NoteFirestoreServiceImpl.Companion.USER_ID
import com.codingwithmitch.cleannotes.framework.datasource.network.mappers.NetworkMapper
import com.codingwithmitch.cleannotes.framework.presentation.TestBaseApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
open class FirestoreTest: BaseTest() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var noteDataFactory: NoteDataFactory

    @Inject
    lateinit var dateUtil: DateUtil

    @Inject
    lateinit var noteFactory: NoteFactory

    @Inject
    lateinit var networkMapper: NetworkMapper

    init {
        injectTest()
        signIn()
        insertTestData()
    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

    fun insertTestData() {
        val entityList = networkMapper.noteListToEntityList(
            noteDataFactory.produceListOfNotes()
        )
        for(entity in entityList){
            firestore
                .collection(NOTES_COLLECTION)
                .document(USER_ID)
                .collection(NOTES_COLLECTION)
                .document(entity.id)
                .set(entity)
        }
    }

    private fun signIn() = runBlocking{
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            NoteFirestoreServiceImpl.EMAIL,
            NoteFirestoreServiceImpl.PASSWORD
        ).await()
    }



}




















