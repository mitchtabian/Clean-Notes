package com.codingwithmitch.notes.di

import com.codingwithmitch.cleannotes.di.features.notes.NotesFeature
import dagger.Component

@FeatureScope
@Component(
    dependencies = [NotesFeature.Dependencies::class],
    modules = [
        NoteModule::class
    ]
)
interface NoteComponent {

    fun notesFeature(): NotesFeature

}