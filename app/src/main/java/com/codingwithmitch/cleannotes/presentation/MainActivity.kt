package com.codingwithmitch.cleannotes.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.BuildConfig
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.service_loader.NotesFeature
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebug"

//    @Inject
//    lateinit var app: BaseApplication

    private val splitInstallManager by lazy{
        SplitInstallManagerFactory.create(application)
    }
    var notesModule: NotesFeature? = null
    private var sessionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
//        (application as BaseApplication).appComponent
//            .inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        splitInstallManager.registerListener(listener)
        saveCounter()
    }

    override fun onDestroy() {
        super.onDestroy()
        splitInstallManager.unregisterListener(listener)
    }

    private val listener = SplitInstallStateUpdatedListener { state ->
//        if (state.sessionId() == sessionId) {
            when (state.status()) {
                SplitInstallSessionStatus.FAILED -> {
                    Log.d(TAG, "Module install failed with ${state.errorCode()}")
                    Toast.makeText(application, "Module install failed with ${state.errorCode()}", Toast.LENGTH_SHORT).show()
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    Toast.makeText(application, "Notes module installed successfully", Toast.LENGTH_SHORT).show()
                    saveCounter()
                }
                else -> Log.d(TAG, "Status: ${state.status()}")
            }
//        }
    }

    fun saveCounter() {
        if (notesModule == null) {
            if (isNotesInstalled()) {
                initializeNotesFeature()
            } else {
                requestNotesInstall()
            }
        }
        if (notesModule != null) {
            val fragment = (notesModule as NotesFeature).provideNoteListFragment()
            Log.d(TAG, "got fragment: ${fragment}")
        }
        else{
            Log.d(TAG, "NotesModule is NULL")
        }
    }

    fun initializeNotesFeature()  {

        // We will need this to pass in dependencies to the StorageFeature.Provider
        val dependencies: NotesFeature.Dependencies = object : NotesFeature.Dependencies {
            override fun noteListFragment(): Fragment = noteListFragment()
        }

        // Ask ServiceLoader for concrete implementations of StorageFeature.Provider
        // Explicitly use the 2-argument version of load to enable R8 optimization.
        val serviceLoader = ServiceLoader.load(
            NotesFeature.Provider::class.java,
            NotesFeature.Provider::class.java.classLoader
        )

        // Explicitly ONLY use the .iterator() method on the returned ServiceLoader to enable R8 optimization.
        // When these two conditions are met, R8 replaces ServiceLoader calls with direct object instantiation.
        notesModule = serviceLoader.iterator().next().get(dependencies)
        Log.d(TAG, "Loaded notes feature through ServiceLoader")
    }

    private fun isNotesInstalled(): Boolean{
//        if (BuildConfig.DEBUG) true
//        else splitInstallManager.installedModules.contains(getString(R.string.module_notes_name))
//        return splitInstallManager.installedModules.contains(getString(R.string.module_notes_name))
        return false
    }

    private fun requestNotesInstall() {
        Toast.makeText(application, "Requesting notes module install", Toast.LENGTH_SHORT).show()
        val request =
            SplitInstallRequest
                .newBuilder()
                .addModule(getString(R.string.module_notes_name))
                .build()

        splitInstallManager
            .startInstall(request)
            .addOnSuccessListener { id -> sessionId = id }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error installing module: ", exception)
                Toast.makeText(application, "Error requesting module install", Toast.LENGTH_SHORT).show()
            }
    }

}

























