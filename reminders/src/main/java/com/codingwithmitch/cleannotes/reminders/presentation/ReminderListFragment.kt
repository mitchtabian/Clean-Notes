package com.codingwithmitch.cleannotes.reminders.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.MyNavController
import com.codingwithmitch.cleannotes.reminders.R
import kotlinx.android.synthetic.main.fragment_reminder_list.*
import java.lang.ClassCastException

class ReminderListFragment : Fragment(R.layout.fragment_reminder_list){


    lateinit var navController: MyNavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reminders_title.setOnClickListener {
            findNavController().navigate(R.id.action_reminders_list_fragment_to_reminderDetailFragment)
        }
        navController.checkBottomNav(getString(R.string.module_reminders_name))
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