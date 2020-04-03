package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.UIController
import com.codingwithmitch.notes.R
import kotlinx.android.synthetic.main.fragment_note_detail.*
import java.lang.ClassCastException


class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {

    lateinit var uiController: UIController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notes_detail_title.setOnClickListener {
            uiController.navRemindersGraph()
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














