package com.codingwithmitch.cleannotes.framework.presentation

import android.app.Application
import com.codingwithmitch.cleannotes.di.AppComponent
import com.codingwithmitch.cleannotes.di.DaggerAppComponent
import kotlinx.coroutines.*

@FlowPreview
@ExperimentalCoroutinesApi
open class BaseApplication : Application(){

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }


    open fun initAppComponent(){
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }


}