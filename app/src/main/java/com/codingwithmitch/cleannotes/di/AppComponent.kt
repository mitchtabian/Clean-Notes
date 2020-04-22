package com.codingwithmitch.cleannotes.di

import com.codingwithmitch.cleannotes.di.features.notes.NotesFeature
import com.codingwithmitch.cleannotes.di.features.notes.NotesFeatureModule
import com.codingwithmitch.cleannotes.di.features.reminders.RemindersFeature
import com.codingwithmitch.cleannotes.di.features.reminders.RemindersFeatureModule
import com.codingwithmitch.cleannotes.framework.presentation.BaseApplication
import com.codingwithmitch.cleannotes.framework.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NotesFeatureModule::class,
        RemindersFeatureModule::class
    ]
)
interface AppComponent :
    NotesFeature.Dependencies,
    RemindersFeature.Dependencies
{

    fun notesFeature(): NotesFeature?

    fun remindersFeature(): RemindersFeature?

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}












