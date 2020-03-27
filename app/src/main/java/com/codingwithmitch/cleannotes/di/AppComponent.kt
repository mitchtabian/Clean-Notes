package com.codingwithmitch.cleannotes.di

import com.codingwithmitch.cleannotes.NotesFeature
import com.codingwithmitch.cleannotes.di.scopes.FeatureModule
import com.codingwithmitch.cleannotes.presentation.BaseApplication
import com.codingwithmitch.cleannotes.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component


@Component(
    modules = [
        AppModule::class,
        FeatureModule::class
    ]
)
interface AppComponent: NotesFeature.Dependencies {

//    fun application(): Application

//    fun dateUtil(): DateUtil

    fun notesFeature(): NotesFeature?

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}












