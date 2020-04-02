package com.codingwithmitch.notes.di

import com.codingwithmitch.cleannotes.di.features.notes.NotesFeature
import com.codingwithmitch.cleannotes.notes.di.NoteViewModelModule
import com.codingwithmitch.cleannotes.notes.presentation.NoteListFragment
import dagger.Component

@FeatureScope
@Component(
    dependencies = [NotesFeature.Dependencies::class],
    modules = [
        NoteModule::class,
        NoteViewModelModule::class
    ]
)
interface NoteComponent {

    fun notesFeature(): NotesFeature

    @Component.Factory
    interface Factory {
        fun create(dependencies: NotesFeature.Dependencies): NoteComponent
    }

    fun inject(noteListFragment: NoteListFragment)
}