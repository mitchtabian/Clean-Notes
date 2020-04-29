package com.codingwithmitch.cleannotes.framework.presentation

import android.app.Application
import com.codingwithmitch.cleannotes.business.interactors.network_sync.SyncNotes
import com.codingwithmitch.cleannotes.di.AppComponent
import com.codingwithmitch.cleannotes.di.DaggerAppComponent
import kotlinx.coroutines.*
import javax.inject.Inject

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