package com.codingwithmitch.cleannotes.notes

import android.content.Context
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.notes.presentation.NoteListFragment
import com.codingwithmitch.cleannotes.NotesFeature
import com.codingwithmitch.notes.di.DaggerNoteComponent
import com.codingwithmitch.notes.di.FeatureScope
import javax.inject.Inject

@FeatureScope
class NotesFeatureImpl
@Inject
constructor(
//    private val appContext: Context
): NotesFeature {


    override fun provideNoteListFragment(): Fragment {
        return NoteListFragment()
    }


    companion object Provider: NotesFeature.Provider{

        override fun get(dependencies: NotesFeature.Dependencies): NotesFeature {
            return DaggerNoteComponent.builder()
                .dependencies(dependencies)
                .build()
                .notesFeature()
        }

    }

}













