package com.codingwithmitch.cleannotes.framework.presentation.notelist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.launchActivity
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.BaseTest
import com.codingwithmitch.cleannotes.di.TestAppComponent
import com.codingwithmitch.cleannotes.framework.presentation.MainActivity
import com.codingwithmitch.cleannotes.framework.presentation.NoteFragmentFactory
import com.codingwithmitch.cleannotes.framework.presentation.TestNoteFragmentFactory
import com.codingwithmitch.cleannotes.framework.presentation.UIController
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class NoteListFragmentTests: BaseTest() {

    @Inject
    lateinit var fragmentFactory: TestNoteFragmentFactory

    val uiController = mockk<UIController>()

    init {
        injectTest()
        setupUIController()
    }

    private fun setupUIController(){
        every { uiController.displayProgressBar(any()) } just runs
        every { uiController.hideSoftKeyboard() } just runs
        every { uiController.displayInputCaptureDialog(any(), any()) } just runs
        every { uiController.onResponseReceived(any(), any()) } just runs
        fragmentFactory.uiController = uiController
    }

    @Test
    fun firstTest(){

        val scenario = launchFragmentInContainer<NoteListFragment>(
            factory = fragmentFactory
        )


    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}

























