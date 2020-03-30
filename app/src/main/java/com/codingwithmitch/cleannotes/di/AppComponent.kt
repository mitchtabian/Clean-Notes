package com.codingwithmitch.cleannotes.di

import com.codingwithmitch.cleannotes.presentation.BaseApplication
import com.codingwithmitch.cleannotes.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component


@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent{

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}












