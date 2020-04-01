package com.codingwithmitch.cleannotes.presentation

interface UIController {

    fun navNotesGraph()

    fun navRemindersGraph()

    fun navSettingsGraph()

    fun checkBottomNav(moduleName: String)

    fun displayBottomNav(isDisplayed: Boolean)

    fun displayProgressBar(isDisplayed: Boolean)

}