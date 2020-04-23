package com.codingwithmitch.cleannotes.framework.datasource.network.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseFirestoreConfig
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
){

    fun auth() = firebaseAuth

    fun firestore() = firestore

    companion object {
        const val NOTES_COLLECTION = "notes"
        const val USERS_COLLECTION = "users"
        const val USER_ID = "9E7fDYAUTNUPFirw4R28NhBZE1u1" // hardcoded for single user
    }

}