package com.codingwithmitch.cleannotes.framework.datasource.network.implementation

import com.codingwithmitch.cleannotes.business.data.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.network.database.FirebaseFirestoreConfig
import com.codingwithmitch.cleannotes.framework.datasource.network.database.FirebaseFirestoreConfig.Companion.NOTES_COLLECTION
import com.codingwithmitch.cleannotes.framework.datasource.network.database.FirebaseFirestoreConfig.Companion.USER_ID
import com.codingwithmitch.cleannotes.framework.datasource.network.mappers.NetworkMapper
import com.codingwithmitch.cleannotes.framework.datasource.network.model.NoteNetworkEntity.Companion.BODY_FIELD
import com.codingwithmitch.cleannotes.framework.datasource.network.model.NoteNetworkEntity.Companion.TITLE_FIELD
import com.codingwithmitch.cleannotes.framework.datasource.network.model.NoteNetworkEntity.Companion.UPDATED_AT_FIELD
import com.codingwithmitch.cleannotes.util.printLogD
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Singleton

/**
 * Firestore doc refs:
 * 1. add:  https://firebase.google.com/docs/firestore/manage-data/add-data
 * 2. delete: https://firebase.google.com/docs/firestore/manage-data/delete-data
 * 3. update: https://firebase.google.com/docs/firestore/manage-data/add-data#update-data
 * 4. query: https://firebase.google.com/docs/firestore/query-data/queries
 */
@Singleton
class NoteNetworkDataSourceImpl
constructor(
    private val config: FirebaseFirestoreConfig,
    private val networkMapper: NetworkMapper,
    private val dateUtil: DateUtil
): NoteNetworkDataSource {

    override suspend fun insertNote(note: Note): Task<Void> {
        val entity = networkMapper.mapToEntity(note)
        return config.firestore()
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(entity.id)
            .set(entity)
    }

    override suspend fun deleteNote(primaryKey: String): Task<Void> {
        return config.firestore()
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(primaryKey)
            .delete()
    }

    override suspend fun updateNote(
        primaryKey: String,
        newTitle: String,
        newBody: String?
    ): Task<Void> {
        val updates = hashMapOf<String, Any>(
            TITLE_FIELD to newTitle
        )
        newBody?.let { body ->
            updates.put(BODY_FIELD, body)
        }
        return config.firestore()
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .document(primaryKey)
            .update(updates)
    }

    override suspend fun findUpdatedNotes(previousSyncTimestamp: Long): Task<QuerySnapshot> {
        return config.firestore()
            .collection(NOTES_COLLECTION)
            .document(USER_ID)
            .collection(NOTES_COLLECTION)
            .whereGreaterThan(
                UPDATED_AT_FIELD,
                dateUtil.convertLongDateToFirebaseTimestamp(previousSyncTimestamp)
            )
            .get()
    }

    override suspend fun insertNotes(notes: List<Note>): List<Task<Void>> {
        val startTime = System.currentTimeMillis()
        return withContext(IO){ // wait for all the tasks to complete
            val results: ArrayList<Task<Void>> = ArrayList()
            for(note in notes){
                launch { // do all jobs in parallel
                    printLogD("NetworkDataSource", "starting job for note: ${note.title}")
                    val entity = networkMapper.mapToEntity(note)
                    val result = config.firestore()
                        .collection(NOTES_COLLECTION)
                        .document(USER_ID)
                        .collection(NOTES_COLLECTION)
                        .document(entity.id)
                        .set(entity)
                    results.add(result)
                }
            }
            printLogD("NetworkDataSource",
                "insertNotes: elapsed time: ${(System.currentTimeMillis() - startTime)} ms")
            results
        }
    }


}





























