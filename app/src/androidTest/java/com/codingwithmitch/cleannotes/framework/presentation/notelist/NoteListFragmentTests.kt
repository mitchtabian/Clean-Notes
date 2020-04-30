package com.codingwithmitch.cleannotes.framework.presentation.notelist

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.codingwithmitch.cleannotes.BaseTest
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.di.TestAppComponent
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.mappers.CacheMapper
import com.codingwithmitch.cleannotes.framework.datasource.cache.model.NoteCacheEntity
import com.codingwithmitch.cleannotes.framework.datasource.data.NoteDataFactory
import com.codingwithmitch.cleannotes.framework.presentation.TestNoteFragmentFactory
import com.codingwithmitch.cleannotes.framework.presentation.UIController
import com.codingwithmitch.cleannotes.framework.presentation.notelist.NoteListAdapter.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.test.assertEquals


/*
    --Test cases:
    1. is the test data being displayed in the RecyclerView?
    2. Make a search using the searchview
    3. clear the search so all list items show again
    3. Toolbar states

    --Notes:
    1. I really dislike these FragmentScenario tests. It seems to randomly fail for no reason.
    And you don't have the option to "clear state" because then you'd have to use test
    orchestrator. And if you use test orchestrator you can't get reports. There's always a
    downside, whichever route you choose.

    2. SearchViews should have an option to "submit an empty query". It's a pain to deal
    with and create work-arounds.


 */
@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class NoteListFragmentTests: BaseTest() {

    @Inject
    lateinit var fragmentFactory: TestNoteFragmentFactory

    @Inject
    lateinit var cacheMapper: CacheMapper

    @Inject
    lateinit var noteDataFactory: NoteDataFactory

    @Inject
    lateinit var dao: NoteDao

    private val testEntityList: ArrayList<NoteCacheEntity> = ArrayList()

    val uiController = mockk<UIController>(relaxed = true)

    val navController = mockk<NavController>(relaxed = true)

    init {
        injectTest()
        val tempList = cacheMapper.noteListToEntityList(
            noteDataFactory.produceListOfNotes()
        )
        // add only 5 notes to the db to prevent need for scrolling recyclerview
        for(index in 0..5){
            testEntityList.add(tempList.get(index))
        }
        insertTestData(testEntityList)
    }

    @Before
    fun before(){
        setupUIController()
    }

    private fun setupUIController(){
        fragmentFactory.uiController = uiController
    }

    private fun insertTestData(testData: List<NoteCacheEntity>) = runBlocking{
        dao.insertNotes(testData)
    }

    /**
     * I decided to write a single large test when testing fragments in isolation.
     * Because if I make multiple tests, they have issues sharing state. I can solve
     * that issue by using test orchestrator, but that will prevent me from getting
     * reports. And I want reports.
     */
    @Test
    fun generalListFragmentTest(){

        // setup
        val scenario = launchFragmentInContainer<NoteListFragment>(
            factory = fragmentFactory
        ).onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), navController)
                }
            }
        }

        // test
        val recyclerView = onView(withId(R.id.recycler_view))

        recyclerView.check(matches(isDisplayed()))

        for(entity in testEntityList){
            onView(withText(entity.title)).check(matches(isDisplayed()))
        }

        // choose a random note and search its title
        val randomNote = testEntityList.get(Random.nextInt(0, testEntityList.size-1))
        onView(withId(R.id.search_view))
            .perform(typeSearchViewTextAndSubmit(randomNote.title))

        // make sure all views except the randomNote title are not visible
        for(entity in testEntityList){
            if(!entity.title.equals(randomNote.title)){
                onView(withText(entity.title)).check(doesNotExist())
            }
        }

        // clear search query to reset the list so all notes are visible
        onView(withId(R.id.search_view))
            .perform(typeSearchViewTextAndSubmit(""))
        scenario.onFragment { fragment ->
            fragment.viewModel.clearSearchQuery()
        }

        for(entity in testEntityList){
            onView(withText(entity.title)).check(matches(isDisplayed()))
        }

        // engage 'MultiSelectionState'
        recyclerView.perform(
            RecyclerViewActions
                .actionOnItemAtPosition<NoteViewHolder>(
                    0,
                    longClick()
                )
        )

        // confirm 'MultiSelectionState' is active
        onView(withId(R.id.action_exit_multiselect_state)).check(matches(isDisplayed()))

        // engage 'SearchViewState'
        onView(withId(R.id.action_exit_multiselect_state)).perform(click())

        // confirm 'SearchViewState' is active
        onView(withId(R.id.search_view)).check(matches(isDisplayed()))

        // select a note and confirm navigate function was called
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<NoteViewHolder>(1, click())
        )
        verify {
            navController.navigate(R.id.action_note_list_fragment_to_noteDetailFragment)
        }
    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}


class typeSearchViewTextAndSubmit(private val text: String?): ViewAction{

    override fun getDescription(): String {
        return "Enter searchview text"
    }

    override fun getConstraints(): Matcher<View> {
        return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
    }

    override fun perform(uiController: UiController?, view: View?) {
        val searchView = (view as SearchView)
        searchView.setQuery(text, true)
    }

}
























