package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.core.framework.*
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.UIController
import com.codingwithmitch.notes.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_note_detail.*
import java.lang.ClassCastException


class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {

    lateinit var uiController: UIController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

        container_due_date.setOnClickListener {
            // TODO("handle click of due date")
        }
    }

    private fun setupUI(){
        uiController.displayBottomNav(false)

        note_detail_app_bar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener{appBar, offset ->

                if(offset < COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD){
                    transitionToCollapsedMode()
                }
                else{
                    transitionToExpandedMode()
                }
            })
    }

    private fun transitionToCollapsedMode(){
        note_title.fadeOut()
        uiController.displayToolbarTitle("List of TODO's", true)
    }

    private fun transitionToExpandedMode(){
        note_title.fadeIn()
        uiController.displayToolbarTitle(null, true)
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














