package com.codingwithmitch.cleannotes.framework.datasource.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 * Disable the use of production data.
 * If you don't use this, the tests will use production data and access the real firestore!
 */
class EmulatorSuite {

    val firestoreSettings = FirebaseFirestoreSettings.Builder()
        .setHost("10.0.2.2:8080")
        .setSslEnabled(false)
        .setPersistenceEnabled(false)
        .build()

    init {
        FirebaseFirestore.getInstance().firestoreSettings = firestoreSettings
    }

    companion object{
        const val CLEAR_EMULATOR_DB = "http://localhost:8080/emulator/v1/projects/clean-notes-11971/databases/(default)/documents"
    }
}