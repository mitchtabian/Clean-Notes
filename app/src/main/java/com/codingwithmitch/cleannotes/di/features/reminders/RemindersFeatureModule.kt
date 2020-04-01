package com.codingwithmitch.cleannotes.di.features.reminders

import android.util.Log
import com.codingwithmitch.cleannotes.di.AppComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

const val PROVIDER_REMINDERS_FEATURE
        = "com.codingwithmitch.cleannotes.reminders.di.RemindersFeatureImpl\$Provider"

@Module
object RemindersFeatureModule {

    private val TAG: String = "AppDebug"

    private var feature: RemindersFeature? = null

    /**
     * This method will return null until the required on-demand feature is installed.
     * It will cache the value the first time a non-null value is returned.
     */
    @Singleton
    @Provides
    @JvmStatic
    fun featureProvider(appComponent: AppComponent): RemindersFeature? {
        if (feature != null){
            return feature as RemindersFeature
        }
        try {
            val provider = Class.forName(PROVIDER_REMINDERS_FEATURE)
                .kotlin.objectInstance as RemindersFeature.Provider
            return provider.getRemindersFeature(appComponent)
                .also { feature = it } //cache the value for later calls
        } catch (e: ClassNotFoundException){
            Log.e(TAG, "Provider class not found", e)
            return null
        }
    }

}










