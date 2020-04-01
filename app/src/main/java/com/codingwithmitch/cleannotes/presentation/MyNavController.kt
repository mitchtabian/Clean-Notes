package com.codingwithmitch.cleannotes.presentation

interface MyNavController {

    fun navNotesGraph()

    fun navRemindersGraph()

    fun navSettingsGraph()

    fun checkBottomNav(moduleName: String)
}