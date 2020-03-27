package com.codingwithmitch.cleannotes.service_loader

import androidx.fragment.app.Fragment

interface NotesFeature {

    fun provideNoteListFragment(): Fragment

    interface Provider{

        fun get(dependencies: Dependencies): NotesFeature
    }

    interface Dependencies {

        fun noteListFragment(): Fragment
    }
}