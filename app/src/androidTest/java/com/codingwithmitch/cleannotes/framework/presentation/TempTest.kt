package com.codingwithmitch.cleannotes.framework.presentation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.di.TestAppComponent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.runner.RunWith
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class TempTest{

    val application: TestBaseApplication
            = ApplicationProvider.getApplicationContext<Context>() as TestBaseApplication

    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

    init {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

    @Test
    fun someRandomTest(){

        assert(::firebaseFirestore.isInitialized)
    }

}

















