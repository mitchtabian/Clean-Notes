package com.codingwithmitch.cleannotes.framework.presentation

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import kotlinx.coroutines.*

@FlowPreview
@ExperimentalCoroutinesApi
open class BaseApplication : Application(){


    override fun onCreate() {
        super.onCreate()
        initCrashlytics()
    }

    private fun initCrashlytics(){
        val core = CrashlyticsCore.Builder().disabled(false).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())
    }


}