package com.codingwithmitch.cleannotes.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.codingwithmitch.cleannotes.R


class MainActivity : AppCompatActivity(),
    MyNavController
{

    private val TAG: String = "AppDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun navNotesGraph() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_notes_graph)
    }

    override fun navSettingsGraph() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_settings_graph)
    }
}

























