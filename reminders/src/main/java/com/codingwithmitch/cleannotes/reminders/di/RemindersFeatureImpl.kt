package com.codingwithmitch.cleannotes.reminders.di

import com.codingwithmitch.cleannotes.di.features.reminders.RemindersFeature
import com.codingwithmitch.cleannotes.reminders.R
import com.codingwithmitch.notes.di.DaggerRemindersComponent
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import javax.inject.Inject

@FeatureScope
class RemindersFeatureImpl
@Inject
constructor(
): RemindersFeature {


    override fun provideTopLevelFragmentId(): Int {
        return R.id.reminders_list_fragment
    }

    companion object Provider: RemindersFeature.Provider {

        override fun getRemindersFeature(
            dependencies: RemindersFeature.Dependencies
        ): RemindersFeature {
            return DaggerRemindersComponent
                .builder()
                .dependencies(dependencies)
                .build()
                .remindersFeature()
        }

    }

}












