package com.codingwithmitch.cleannotes.reminders.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.UIController
import com.codingwithmitch.cleannotes.reminders.R
import kotlinx.android.synthetic.main.fragment_reminder_detail.*
import java.lang.ClassCastException

class ReminderDetailFragment : Fragment(R.layout.fragment_reminder_detail){


    lateinit var uiController: UIController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reminder_detail_title.setOnClickListener {
            uiController.navSettingsGraph()
        }
        setupUI()
    }

    private fun setupUI(){
        uiController.displayBottomNav(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            uiController = context as MainActivity
        }catch (e: ClassCastException){
            e.printStackTrace()
        }
    }
}