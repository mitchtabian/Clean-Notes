package com.codingwithmitch.notes.di

import com.codingwithmitch.cleannotes.NotesFeature
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