package com.codingwithmitch.cleannotes.di.scopes

import android.util.Log
import com.codingwithmitch.cleannotes.NotesFeature
import com.codingwithmitch.cleannotes.di.AppComponent
import dagger.Module
import dagger.Provides

const val PROVIDER_NOTES_FEATURE = "com.codingwithmitch.cleannotes.notes.NotesFeatureImpl\$Provider"

@Module
object FeatureModule {

    private val TAG: String = "AppDebug"

    private var notesFeature: NotesFeature? = null

    /**
     * This method will return null until the required on-demand feature is installed.
     * It will cache the value the first time a non-null value is returned.
     */
    @Provides
    @JvmStatic
    fun storageFeatureProvider(appComponent: AppComponent): NotesFeature? {
        if (notesFeature != null){
            return notesFeature as NotesFeature
        }
        try {
            // Get the instance of the StorageFeature.Provider, pass it the BaseComponent which fulfills the
            // StorageFeature.Dependencies contract, and get the StorageFeature instance in return.
            val provider = Class.forName(PROVIDER_NOTES_FEATURE)
                .kotlin.objectInstance as NotesFeature.Provider
            return provider.get(appComponent)
                .also { notesFeature = it } //cache the value for later calls
        } catch (e: ClassNotFoundException){
            Log.e(TAG, "Provider class not found", e)
            return null
        }
    }
}