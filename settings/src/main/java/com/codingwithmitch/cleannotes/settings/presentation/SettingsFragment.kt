package com.codingwithmitch.cleannotes.settings.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.MyNavController
import com.codingwithmitch.settings.R
import kotlinx.android.synthetic.main.fragment_settings.*
import java.lang.ClassCastException


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    lateinit var navController: MyNavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings_title.setOnClickListener {
            navController.navNotesGraph()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            navController = context as MainActivity
        }catch (e: ClassCastException){
            e.printStackTrace()
        }
    }
}