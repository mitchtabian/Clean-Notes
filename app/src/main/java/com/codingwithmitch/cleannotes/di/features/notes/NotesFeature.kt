package com.codingwithmitch.cleannotes.di.features.notes

interface NotesFeature{

    // From notes module required by app
    fun provideTopLevelFragmentId(): Int

    interface Provider {

        fun getNotesFeature(dependencies: Dependencies): NotesFeature
    }

    // From app required by notes module
    interface Dependencies{

    }
}