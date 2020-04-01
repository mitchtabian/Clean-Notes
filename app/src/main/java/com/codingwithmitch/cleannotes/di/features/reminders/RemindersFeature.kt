package com.codingwithmitch.cleannotes.di.features.reminders

interface RemindersFeature{

    // From notes module required by app
    fun provideTopLevelFragmentId(): Int

    interface Provider {

        fun getRemindersFeature(dependencies: Dependencies): RemindersFeature
    }

    // From app required by notes module
    interface Dependencies{

    }
}