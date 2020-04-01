package com.codingwithmitch.cleannotes.di.features.notes

import android.util.Log
import com.codingwithmitch.cleannotes.di.AppComponent
import com.codingwithmitch.notes.di.FeatureScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

const val PROVIDER_NOTES_FEATURE
        = "com.codingwithmitch.cleannotes.notes.di.NotesFeatureImpl\$Provider"

@Module
object NotesFeatureModule{

    private val TAG: String = "AppDebug"

    private var notesFeature: NotesFeature? = null

    /**
     * This method will return null until the required on-demand feature is installed.
     * It will cache the value the first time a non-null value is returned.
     */
    @Singleton
    @Provides
    @JvmStatic
    fun notesFeatureProvider(appComponent: AppComponent): NotesFeature? {
        if (notesFeature != null){
            return notesFeature as NotesFeature
        }
        try {
            val provider = Class.forName(PROVIDER_NOTES_FEATURE)
                .kotlin.objectInstance as NotesFeature.Provider
            return provider.getNotesFeature(appComponent)
                .also { notesFeature = it } //cache the value for later calls
        } catch (e: ClassNotFoundException){
            Log.e(TAG, "Provider class not found", e)
            return null
        }
    }

}










