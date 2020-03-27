package com.codingwithmitch.cleannotes.presentation

import com.codingwithmitch.cleannotes.di.AppComponent
import com.codingwithmitch.cleannotes.di.DaggerAppComponent
import com.google.android.play.core.splitcompat.SplitCompatApplication

class BaseApplication : SplitCompatApplication(){

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()

    }

    fun initAppComponent(){
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }
}