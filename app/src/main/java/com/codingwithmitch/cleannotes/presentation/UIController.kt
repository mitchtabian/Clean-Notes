package com.codingwithmitch.cleannotes.presentation

import com.codingwithmitch.cleannotes.core.business.state.Response
import com.codingwithmitch.cleannotes.core.business.state.StateMessageCallback

interface UIController {

    fun navNotesGraph()

    fun navRemindersGraph()

    fun navSettingsGraph()

    fun checkBottomNav(moduleName: String)

    fun displayBottomNav(isDisplayed: Boolean)

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}


















