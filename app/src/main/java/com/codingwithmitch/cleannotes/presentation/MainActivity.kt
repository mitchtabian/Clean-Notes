package com.codingwithmitch.cleannotes.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.core.business.state.AreYouSureCallback
import com.codingwithmitch.cleannotes.core.business.state.MessageType
import com.codingwithmitch.cleannotes.core.business.state.Response
import com.codingwithmitch.cleannotes.core.business.state.StateMessageCallback
import com.codingwithmitch.cleannotes.core.business.state.UIComponentType.*
import com.codingwithmitch.cleannotes.core.framework.displayToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    UIController
{

    private val TAG: String = "AppDebug"

    private val bottomNavView: BottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
    }
    private var appBarConfiguration: AppBarConfiguration? = null

    private val topLevelFragmentIds = ArrayList<Int>()

    private var dialogInView: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrieveTopLevelFragmentIds()
        setupActionBar()
        setupBottomNavigation()

    }

    private fun retrieveTopLevelFragmentIds(){
        initializeNotesFeature()
        initializeRemindersFeature()
    }

    fun initializeRemindersFeature() {
        val remindersModule = (application as BaseApplication).appComponent
            .remindersFeature()
        if (remindersModule != null) {
            topLevelFragmentIds.add(remindersModule.provideTopLevelFragmentId())
        }
    }

    fun initializeNotesFeature() {
        val notesModule = (application as BaseApplication).appComponent
            .notesFeature()
        if (notesModule != null) {
            topLevelFragmentIds.add(notesModule.provideTopLevelFragmentId())
        }
    }

    private fun setupBottomNavigation(){
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = topLevelFragmentIds.toSet()
        )
        setupActionBarWithNavController(
            navController,
            appBarConfiguration as AppBarConfiguration
        )

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
        getSupportActionBar()?.setTitle("")
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

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment)
            .navigateUp(appBarConfiguration as AppBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {

        when(response.uiComponentType){

            is AreYouSureDialog -> {

                response.message?.let {
                    areYouSureDialog(
                        message = it,
                        callback = (response.uiComponentType as AreYouSureDialog).callback,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is Toast -> {
                response.message?.let {
                    displayToast(
                        message = it,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is None -> {
                // This would be a good place to send to your Error Reporting
                // software of choice (ex: Firebase crash reporting)
                Log.i(TAG, "onResponseReceived: ${response.message}")
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }

    private fun displayDialog(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ){
        Log.d(TAG, "displayDialog: ")
        response.message?.let { message ->

            dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    displayErrorDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Success -> {
                    displaySuccessDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Info -> {
                    displayInfoDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        }?: stateMessageCallback.removeMessageFromStack()
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onPause() {
        super.onPause()
        if(dialogInView != null){
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }

    private fun displaySuccessDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_success)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayErrorDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_error)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayInfoDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_info)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun areYouSureDialog(
        message: String,
        callback: AreYouSureCallback,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.are_you_sure)
                message(text = message)
                negativeButton(R.string.text_cancel){
                    callback.cancel()
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                positiveButton(R.string.text_yes){
                    callback.proceed()
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

}

























