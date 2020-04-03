package com.codingwithmitch.cleannotes.reminders.di

import com.codingwithmitch.cleannotes.di.features.reminders.RemindersFeature
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import dagger.Module
import dagger.Provides

@Module
object RemindersModule {

    @FeatureScope
    @Provides
    @JvmStatic
    internal fun provideFeatureImpl(
        featureImpl: RemindersFeatureImpl
    ): RemindersFeature {
        return featureImpl
    }
}