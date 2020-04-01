package com.codingwithmitch.cleannotes.notes.di

import com.codingwithmitch.cleannotes.di.features.notes.NotesFeature
import com.codingwithmitch.notes.R
import com.codingwithmitch.notes.di.DaggerNoteComponent
import com.codingwithmitch.notes.di.FeatureScope
import javax.inject.Inject

@FeatureScope
class NotesFeatureImpl
@Inject
constructor(
): NotesFeature {


    override fun provideTopLevelFragmentId(): Int {
        return R.id.note_list_fragment
    }

    companion object Provider: NotesFeature.Provider {

        override fun getNotesFeature(
            dependencies: NotesFeature.Dependencies
        ): NotesFeature {
            return DaggerNoteComponent
                .builder()
                .dependencies(dependencies)
                .build()
                .notesFeature()
        }

    }

}












