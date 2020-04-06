package com.codingwithmitch.cleannotes.notes.di

import com.codingwithmitch.cleannotes.di.features.notes.NotesFeature
import com.codingwithmitch.notes.R
import com.codingwithmitch.notes.di.DaggerNoteComponent
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.notes.di.NoteComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@FeatureScope
class NotesFeatureImpl
@Inject
constructor(
): NotesFeature {

    override fun provideTopLevelFragmentId(): Int {
        return R.id.note_list_fragment
    }

    fun getProvider(): Provider{
        return Provider
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    object Provider: NotesFeature.Provider{

        var noteComponent: NoteComponent? = null

        override fun getNotesFeature(dependencies: NotesFeature.Dependencies): NotesFeature {
            if(noteComponent == null){
                noteComponent = DaggerNoteComponent
                    .factory()
                    .create(dependencies)
                printLogD("NotesFeatureImpl", "setup noteComponent: ${noteComponent}")
            }
            return (noteComponent as NoteComponent).notesFeature()
        }

    }


}












