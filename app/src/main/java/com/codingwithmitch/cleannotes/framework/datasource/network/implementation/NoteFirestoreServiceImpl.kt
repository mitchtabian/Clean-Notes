package com.codingwithmitch.cleannotes.framework.datasource.network.implementation

import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.network.abstraction.NoteFirestoreService
import com.codingwithmitch.cleannotes.framework.datasource.network.mappers.NetworkMapper
import com.codingwithmitch.cleannotes.framework.datasource.network.model.NoteNetworkEntity
import com.codingwithmitch.cleannotes.util.printLogD
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Firestore doc refs:
 * 1. add:  https://firebase.google.com/docs/firestore/manage-data/add-data
 * 2. delete: https://firebase.google.com/docs/firestore/manage-data/delete-data
 * 3. update: https://firebase.google.com/docs/firestore/manage-data/add-data#update-data
 * 4. query: https://firebase.google.com/docs/firestore/query-data/queries
 */
@Singleton
class NoteFirestoreServiceImpl
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth, // might include auth in the future
    private val firestore: FirebaseFirestore,
    private val networkMapper: NetworkMapper,
    private val dateUtil: DateUtil
): NoteFirestoreService {

    override suspend fun insertOrUpdateNote(note: Note): Task<Void> {
        val entity = networkMapper.mapToEntity(note)
        entity.updated_at = Timestamp.now() // for updates
        return firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(entity.id)
            .set(entity)
    }

    override suspend fun deleteNote(primaryKey: String): Task<Void> {
        return firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(primaryKey)
            .delete()
    }

    override suspend fun insertDeletedNote(note: Note): Task<Void> {
        val entity = networkMapper.mapToEntity(note)
        return firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(entity.id)
            .set(entity)
    }

    override suspend fun insertDeletedNotes(notes: List<Note>): List<Task<Void>> {
        val startTime = System.currentTimeMillis()
        return withContext(Dispatchers.IO){ // wait for all the tasks to complete
            val results: ArrayList<Task<Void>> = ArrayList()
            for(note in notes){
                launch { // do all jobs in parallel
                    printLogD("NoteFirestoreServiceImpl",
                        "insertDeletedNotes: starting job for note: ${note.title}")
                    val entity = networkMapper.mapToEntity(note)
                    val result = firestore
                        .collection(DELETES_COLLECTION)
                        .document(USER_ID)
                        .collection(NOTES_COLLECTION)
                        .document(entity.id)
                        .set(entity)
                    results.add(result)
                }
            }
            printLogD("NoteFirestoreServiceImpl",
                "insertDeletedNotes: elapsed time: ${(System.currentTimeMillis() - startTime)} ms")
            results
        }
    }

    override suspend fun deleteDeletedNote(note: Note): Task<Void> {
        val entity = networkMapper.mapToEntity(note)
        return firestore
            .collection(DELETES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(entity.id)
            .delete()
    }

    override suspend fun searchNote(note: Note): Task<DocumentSnapshot> {
        return firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(note.id)
            .get()
    }

    override suspend fun getAllNotes(): Task<QuerySnapshot> {
        return firestore
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .get()
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>): List<Task<Void>> {
        val startTime = System.currentTimeMillis()
        return withContext(Dispatchers.IO){ // wait for all the tasks to complete
            val results: ArrayList<Task<Void>> = ArrayList()
            for(note in notes){
                launch { // do all jobs in parallel
                    printLogD("NoteFirestoreServiceImpl",
                        "insertNotes: starting job for note: ${note.title}")
                    val entity = networkMapper.mapToEntity(note)
                    entity.updated_at = Timestamp.now() // for updates
                    val result = firestore
                        .collection(NOTES_COLLECTION)
                        .document(USER_ID)
                        .collection(NOTES_COLLECTION)
                        .document(entity.id)
                        .set(entity)
                    results.add(result)
                }
            }
            printLogD("NoteFirestoreServiceImpl",
                "insertNotes: elapsed time: ${(System.currentTimeMillis() - startTime)} ms")
            results
        }
    }

    companion object {
        const val NOTES_COLLECTION = "notes"
        const val USERS_COLLECTION = "users"
        const val DELETES_COLLECTION = "deletes"
        const val USER_ID = "9E7fDYAUTNUPFirw4R28NhBZE1u1" // hardcoded for single user
        const val PASSWORD = "HR19jyR5r2h5k8lY"
        const val EMAIL = "mitch@tabian.ca"
    }


}












