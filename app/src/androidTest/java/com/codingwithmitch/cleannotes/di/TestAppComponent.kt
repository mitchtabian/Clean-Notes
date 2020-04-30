package com.codingwithmitch.cleannotes.di

import com.codingwithmitch.cleannotes.framework.datasource.cache.CacheTest
import com.codingwithmitch.cleannotes.framework.datasource.network.FirestoreTest
import com.codingwithmitch.cleannotes.framework.presentation.TestBaseApplication
import com.codingwithmitch.cleannotes.framework.presentation.notedetail.NoteDetailFragmentTests
import com.codingwithmitch.cleannotes.framework.presentation.notelist.NoteListFragmentTests
import com.codingwithmitch.cleannotes.notes.di.NoteViewModelModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
@Singleton
@Component(
    modules = [
        TestAppModule::class,
        TempModule::class,
        TestNoteFragmentFactoryModule::class,
        NoteViewModelModule::class
    ]
)
interface TestAppComponent: AppComponent {

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: TestBaseApplication): TestAppComponent
    }

    fun inject(cacheTest: CacheTest)

    fun inject(firestoreTest: FirestoreTest)

    fun inject(noteListFragmentTests: NoteListFragmentTests)

    fun inject(noteDetailFragmentTests: NoteDetailFragmentTests)

}
















