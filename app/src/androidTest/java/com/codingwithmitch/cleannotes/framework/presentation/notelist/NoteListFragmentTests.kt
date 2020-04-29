package com.codingwithmitch.cleannotes.framework.presentation.notelist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class NoteListFragmentTests {

    @Test
    fun firstTest(){

        val scenario = launchFragmentInContainer<NoteListFragment>()
        scenario.onFragment { fragment ->

        }


    }
}

























