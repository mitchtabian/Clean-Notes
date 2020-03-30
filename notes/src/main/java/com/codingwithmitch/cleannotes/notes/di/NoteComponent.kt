package com.codingwithmitch.notes.di

import com.codingwithmitch.cleannotes.di.AppComponent
import com.codingwithmitch.cleannotes.notes.presentation.NoteListFragment
import dagger.Component

@FeatureScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        NoteModule::class
    ]
)
interface NoteComponent {

    @Component.Factory
    interface Factory {

        fun create(appComponent: AppComponent): NoteComponent

    }

    fun inject(noteListFragment: NoteListFragment)
}