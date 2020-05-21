package com.codingwithmitch.cleannotes.framework.presentation

import android.app.Application
import kotlinx.coroutines.*

@FlowPreview
@ExperimentalCoroutinesApi
open class BaseApplication : Application(){


    override fun onCreate() {
        super.onCreate()
    }




}