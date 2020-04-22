package com.codingwithmitch.cleannotes.di.features.notes

import com.codingwithmitch.cleannotes.framework.presentation.BaseApplication
import com.codingwithmitch.cleannotes.core.business.DateUtil


interface NotesFeature{

    // From notes module required by app
    fun provideTopLevelFragmentId(): Int

    interface Provider {

        fun getNotesFeature(dependencies: Dependencies): NotesFeature
    }

    // From app required by notes module
    interface Dependencies{

        fun application(): BaseApplication

        fun dateUtil(): DateUtil

    }
}