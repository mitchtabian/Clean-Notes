package com.codingwithmitch.cleannotes

import android.content.Context
import androidx.fragment.app.Fragment

interface NotesFeature {

    fun provideNoteListFragment(): Fragment

    interface Provider{

        fun get(dependencies: Dependencies): NotesFeature
    }

    /**
     * Dependencies from the app module that are required by the feature
     */
    interface Dependencies {

//        fun appContext(): Context
    }
}