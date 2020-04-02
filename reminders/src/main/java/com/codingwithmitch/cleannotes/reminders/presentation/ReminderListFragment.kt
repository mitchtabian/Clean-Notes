package com.codingwithmitch.cleannotes.reminders.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.UIController
import com.codingwithmitch.cleannotes.reminders.R
import kotlinx.android.synthetic.main.fragment_reminder_list.*
import java.lang.ClassCastException

class ReminderListFragment : Fragment(R.layout.fragment_reminder_list){


    lateinit var uiController: UIController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reminders_title.setOnClickListener {
            findNavController().navigate(R.id.action_reminders_list_fragment_to_reminderDetailFragment)
        }
        setupUI()
    }

    private fun setupUI(){
        uiController.checkBottomNav(getString(com.codingwithmitch.cleannotes.R.string.module_reminders_name))
        uiController.displayBottomNav(true)
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