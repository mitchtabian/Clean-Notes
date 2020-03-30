package com.codingwithmitch.cleannotes.notes.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.MyNavController
import com.codingwithmitch.notes.R
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.fragment_note_list.*
import java.lang.ClassCastException


class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {

    lateinit var navController: MyNavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notes_detail_title.setOnClickListener {
            navController.navRemindersGraph()
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














