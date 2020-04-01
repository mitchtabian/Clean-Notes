package com.codingwithmitch.cleannotes.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.findNavController
import com.codingwithmitch.cleannotes.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    MyNavController,
    UIController
{

    private val TAG: String = "AppDebug"

    private val bottomNavView: BottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupBottomNavigation()
    }

    private fun setupBottomNavigation(){

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

























