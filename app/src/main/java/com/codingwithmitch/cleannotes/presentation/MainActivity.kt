package com.codingwithmitch.cleannotes.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.codingwithmitch.cleannotes.R
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    UIController
{

    private val TAG: String = "AppDebug"

    private val bottomNavView: BottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
    }

    private val topLevelFragmentIds = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeNotesFeature()
        initializeRemindersFeature()

        setupActionBar()
        setupBottomNavigation()
    }

    fun initializeRemindersFeature() {
        val remindersModule = (application as BaseApplication).appComponent.remindersFeature()
        if (remindersModule != null) {
            Log.d(TAG, "Loaded reminders feature through dagger: " +
                    "${remindersModule.provideTopLevelFragmentId()}")
            topLevelFragmentIds.add(remindersModule.provideTopLevelFragmentId())
        }
    }

    fun initializeNotesFeature() {
        val notesModule = (application as BaseApplication).appComponent.notesFeature()
        if (notesModule != null) {
            Log.d(TAG, "Loaded notes feature through dagger: " +
                    "${notesModule.provideTopLevelFragmentId()}")
            topLevelFragmentIds.add(notesModule.provideTopLevelFragmentId())
        }
    }

    private fun setupBottomNavigation(){
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = topLevelFragmentIds.toSet()
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            onNavigationItemSelected(menuItem.itemId)
        }
    }

    private fun onNavigationItemSelected(menuItemId: Int): Boolean{
        when(menuItemId){

            R.id.menu_nav_notes -> {
                navNotesGraph()
            }

            R.id.menu_nav_reminders -> {
                navRemindersGraph()
            }
        }
        return true
    }

    private fun setupActionBar(){
        setSupportActionBar(tool_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_nav_settings -> {
                navSettingsGraph()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayBottomNav(isDisplayed: Boolean) {
        if(isDisplayed)
            bottom_navigation_view.visibility = View.VISIBLE
        else
            bottom_navigation_view.visibility = View.GONE
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        if(isDisplayed)
            main_progress_bar.visibility = View.VISIBLE
        else
            main_progress_bar.visibility = View.GONE
    }

    override fun navNotesGraph() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_notes_graph)
    }

    override fun navSettingsGraph() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_settings_graph)
    }

    override fun navRemindersGraph() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_reminders_graph)
    }

    override fun checkBottomNav(moduleName: String) {
        when(moduleName){

            getString(R.string.module_notes_name) ->{
                bottomNavView.menu.findItem(R.id.menu_nav_notes).isChecked = true
            }

            getString(R.string.module_reminders_name) ->{
                bottomNavView.menu.findItem(R.id.menu_nav_reminders).isChecked = true
            }
        }
    }
}

























