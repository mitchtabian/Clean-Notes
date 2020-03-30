package com.codingwithmitch.notes.di

import com.codingwithmitch.cleannotes.di.AppComponent
import dagger.Component

@FeatureScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        NoteModule::class
    ]
)
interface NoteComponent {


}