package com.codingwithmitch.cleannotes.di

import com.codingwithmitch.cleannotes.framework.presentation.BaseApplication
import com.codingwithmitch.cleannotes.framework.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [
        AppModule::class,
        ProductionModule::class
    ]
)
interface AppComponent{

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}