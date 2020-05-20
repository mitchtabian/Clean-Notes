package com.codingwithmitch.cleannotes.framework.datasource.network

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.di.TestAppComponent
import com.codingwithmitch.cleannotes.framework.datasource.network.abstraction.NoteFirestoreService
import com.codingwithmitch.cleannotes.framework.datasource.network.implementation.NoteFirestoreServiceImpl
import com.codingwithmitch.cleannotes.framework.datasource.network.mappers.NetworkMapper
import com.codingwithmitch.cleannotes.framework.presentation.TestBaseApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class NoteFirestoreServiceTests{

    // system in test
    private lateinit var noteFirestoreService: NoteFirestoreService

    // dependencies
    val application: TestBaseApplication
            = ApplicationProvider.getApplicationContext<Context>() as TestBaseApplication

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var noteFactory: NoteFactory

    @Inject
    lateinit var networkMapper: NetworkMapper

    init {
        (application.appComponent as TestAppComponent)
            .inject(this)
        signIn()
    }

    @Before
    fun before(){
        noteFirestoreService = NoteFirestoreServiceImpl(
            firebaseAuth = FirebaseAuth.getInstance(),
            firestore = firestore,
            networkMapper = networkMapper
        )
    }

    private fun signIn() = runBlocking{
        firebaseAuth.signInWithEmailAndPassword(
            EMAIL,
            PASSWORD
        ).await()
    }

    @Test
    fun insertSingleNote_CBS() = runBlocking{
        val note = noteFactory.createSingleNote(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )

        noteFirestoreService.insertOrUpdateNote(note)

        val searchResult = noteFirestoreService.searchNote(note)

        assertEquals(note, searchResult)
    }

    companion object{
        const val EMAIL = "mitchtest@tabian.ca"
        const val PASSWORD = "password"
    }
}

















