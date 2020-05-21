package com.codingwithmitch.cleannotes.di

import com.codingwithmitch.cleannotes.framework.datasource.cache.NoteDaoServiceTests
import com.codingwithmitch.cleannotes.framework.datasource.network.NoteFirestoreServiceTests
import com.codingwithmitch.cleannotes.framework.presentation.TestBaseApplication
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
@Component(
    modules = [
        TestModule::class,
        AppModule::class
    ]
)
interface TestAppComponent: AppComponent {

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: TestBaseApplication): TestAppComponent
    }

    fun inject(noteFirestoreServiceTests: NoteFirestoreServiceTests)

    fun inject(noteDaoServiceTests: NoteDaoServiceTests)

}













