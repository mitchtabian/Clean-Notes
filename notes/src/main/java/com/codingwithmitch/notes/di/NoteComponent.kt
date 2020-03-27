package com.codingwithmitch.notes.di

import com.codingwithmitch.cleannotes.di.AppComponent
import com.codingwithmitch.notes.presentation.ListActivity
import dagger.Component

@NoteScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        NoteModule::class
    ]
)
interface NoteComponent {

    @Component.Factory
    interface Factory{

        fun create(appComponent: AppComponent): NoteComponent
    }

    fun inject(listActivity: ListActivity)
}