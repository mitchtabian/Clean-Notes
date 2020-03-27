package com.codingwithmitch.cleannotes.presentation

import android.app.Application
import com.codingwithmitch.cleannotes.di.AppComponent
import com.codingwithmitch.cleannotes.di.DaggerAppComponent

class BaseApplication : Application(){

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