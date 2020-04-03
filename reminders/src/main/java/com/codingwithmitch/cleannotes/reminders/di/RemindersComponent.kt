package com.codingwithmitch.notes.di

import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.cleannotes.di.features.reminders.RemindersFeature
import com.codingwithmitch.cleannotes.reminders.di.RemindersModule
import dagger.Component

@FeatureScope
@Component(
    dependencies = [RemindersFeature.Dependencies::class],
    modules = [
        RemindersModule::class
    ]
)
interface RemindersComponent {

    fun remindersFeature(): RemindersFeature

}