package com.codingwithmitch.notes.di

import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.cleannotes.di.features.notes.NotesFeature
import com.codingwithmitch.cleannotes.notes.di.NoteViewModelModule
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.NoteDetailFragment
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.NoteListFragment
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
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

    fun inject(noteDetailFragment: NoteDetailFragment)
}