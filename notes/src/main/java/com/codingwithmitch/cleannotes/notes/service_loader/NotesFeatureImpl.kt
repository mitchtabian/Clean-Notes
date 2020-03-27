package com.codingwithmitch.cleannotes.notes.service_loader

import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.notes.presentation.NoteListFragment
import com.codingwithmitch.cleannotes.service_loader.NotesFeature

class NotesFeatureImpl(
    val noteListFragment: NoteListFragment
): NotesFeature{

    override fun provideNoteListFragment(): Fragment {
        return noteListFragment
    }


}

/**
 * The provider class. It cannot be a singleton as it has to be instantiable
 * through a default constructor to satisfy ServiceLoader requirements.
 *
 */
class NotesFeatureProviderImpl : NotesFeature.Provider {
    override fun get(dependencies: NotesFeature.Dependencies): NotesFeature {
        return NotesFeatureImpl(dependencies.noteListFragment() as NoteListFragment)
    }
}












