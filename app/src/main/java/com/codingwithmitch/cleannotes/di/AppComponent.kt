package com.codingwithmitch.cleannotes.di

import android.app.Application
import com.codingwithmitch.cleannotes.util.DateUtil
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {

    fun application(): Application

    fun dateUtil(): DateUtil

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: Application): AppComponent
    }
}












