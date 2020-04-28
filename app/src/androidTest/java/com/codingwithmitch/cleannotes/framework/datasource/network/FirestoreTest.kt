package com.codingwithmitch.cleannotes.framework.datasource.network

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.network.implementation.NoteFirestoreServiceImpl.Companion.NOTES_COLLECTION
import com.codingwithmitch.cleannotes.framework.datasource.network.implementation.NoteFirestoreServiceImpl.Companion.USER_ID
import com.codingwithmitch.cleannotes.framework.datasource.network.mappers.NetworkMapper
import com.codingwithmitch.cleannotes.framework.datasource.network.model.NoteNetworkEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4ClassRunner::class)
class FirestoreTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val dateUtil: DateUtil = DateUtil(dateFormat)
    private val noteFactory = NoteFactory(dateUtil)
    private val networkMapper = NetworkMapper(dateUtil)

    val firestoreSettings = FirebaseFirestoreSettings.Builder()
        .setHost("10.0.2.2:8080")
        .setSslEnabled(false)
        .setPersistenceEnabled(false)
        .build()

    val firestore = FirebaseFirestore.getInstance()

    init {
        FirebaseFirestore.getInstance().firestoreSettings = firestoreSettings
    }

    private fun insertTestDataSet() = runBlocking {

    }

    @Test
    fun firstTest() = runBlocking{

        val note = noteFactory.createSingleNote(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )
        val entity = networkMapper.mapToEntity(note)

        firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(entity.id)
            .set(entity)
            .addOnCompleteListener {
                println("test: inserted new note: ${entity.title}")
            }
            .addOnFailureListener {
                println("test: failure: ${it}")
                assertEquals(0, 1)
            }
            .await()


        val noteList = firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .get()
            .await()
            .toObjects(NoteNetworkEntity::class.java)

        println("test: size: ${noteList.size}")
        for(noteEntity in noteList){
            println("test: ${noteEntity.title}")
        }
    }

}




















