package com.codingwithmitch.cleannotes.framework.presentation.notedetail

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.BaseTest
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.di.TestAppComponent
import com.codingwithmitch.cleannotes.framework.datasource.cache.mappers.CacheMapper
import com.codingwithmitch.cleannotes.framework.datasource.data.NoteDataFactory
import com.codingwithmitch.cleannotes.framework.presentation.TestNoteFragmentFactory
import com.codingwithmitch.cleannotes.framework.presentation.UIController
import com.codingwithmitch.cleannotes.util.EspressoIdlingResourceRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject

/*
    --Test cases:
    1. Is the selected note retrieve from the bundle args and set properly?
    2. Confirm toolbar states are toggling properly
    3. Confirm backstack is popped if "back arrow" is clicked in toolbar

 */
@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class NoteDetailFragmentTests: BaseTest() {

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var fragmentFactory: TestNoteFragmentFactory

    @Inject
    lateinit var cacheMapper: CacheMapper

    @Inject
    lateinit var noteDataFactory: NoteDataFactory

    private val testNote: Note

    val uiController = mockk<UIController>(relaxed = true)

    val navController = mockk<NavController>(relaxed = true)

    init {
        injectTest()
        testNote = noteDataFactory.createSingleNote(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString()
        )
    }

    @Before
    fun before(){
        setupUIController()
    }

    private fun setupUIController(){
        fragmentFactory.uiController = uiController
    }

    /**
     * I decided to write a single large test when testing fragments in isolation.
     * Because if I make multiple tests, they have issues sharing state. I can solve
     * that issue by using test orchestrator, but that will prevent me from getting
     * reports. And I want reports.
     */
    @Test
    fun generalDetailFragmentTest(){

        // setup
        val scenario = launchFragmentInContainer<NoteDetailFragment>(
            factory = fragmentFactory,
            fragmentArgs = bundleOf(NOTE_DETAIL_SELECTED_NOTE_BUNDLE_KEY to testNote)
        ).onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), navController)
                }
            }
        }

        // test

        // confirm arguments are set from bundle
        onView(withId(R.id.note_title)).check(matches(withText(testNote.title)))
        onView(withId(R.id.note_body)).check(matches(withText(testNote.body)))

        // confirm toolbar states are toggling properly
        onView(withId(R.id.note_title)).check(matches(withText(testNote.title)))
        onView(withId(R.id.tool_bar_title)).check(matches(not(isDisplayed())))

        onView(withId(R.id.note_body_container)).perform(swipeUp())

        onView(withId(R.id.note_title)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tool_bar_title)).check(matches(withText(testNote.title)))

        onView(withId(R.id.note_body_container)).perform(swipeDown())

        onView(withId(R.id.note_title)).check(matches(withText(testNote.title)))
        onView(withId(R.id.tool_bar_title)).check(matches(not(isDisplayed())))

        // press back button
        onView(withId(R.id.toolbar_primary_icon)).perform(click())

        // confirm NavController attempted to navigate
        verify {
            navController.popBackStack()
        }

    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}

























