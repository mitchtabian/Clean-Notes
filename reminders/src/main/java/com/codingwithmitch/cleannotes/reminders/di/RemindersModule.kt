package com.codingwithmitch.cleannotes.reminders.di

import com.codingwithmitch.cleannotes.di.features.notes.NotesFeature
import com.codingwithmitch.cleannotes.di.features.reminders.RemindersFeature
import com.codingwithmitch.notes.di.FeatureScope
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