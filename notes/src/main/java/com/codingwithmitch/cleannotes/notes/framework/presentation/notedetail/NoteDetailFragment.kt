package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.core.framework.*
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.di.NotesFeatureImpl
import com.codingwithmitch.cleannotes.notes.framework.presentation.BaseNoteFragment
import com.codingwithmitch.cleannotes.notes.framework.presentation.NoteViewModel
import com.codingwithmitch.cleannotes.presentation.BaseApplication
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.UIController
import com.codingwithmitch.notes.R
import com.codingwithmitch.notes.di.DaggerNoteComponent
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.ClassCastException
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class NoteDetailFragment : BaseNoteFragment(R.layout.fragment_note_detail) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: NoteViewModel by viewModels {
        viewModelFactory
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

        container_due_date.setOnClickListener {
            // TODO("handle click of due date")
        }

        subscribeObservers()

        printLogD("NoteDetailFragment", "viewmodel: $viewModel")
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null){
                viewState.noteDetailViewState.let { noteDetailViewState ->
                    noteDetailViewState.note?.let { note ->
                        setNoteTitle(note.title)
                    }
                }
            }
        })
    }

    private fun setNoteTitle(title: String){
        note_title.setText(title)
    }

    private fun getNoteTitle(): String{
        return note_title.text.toString()
    }

    private fun setNoteBody(body: String?){
        note_body.setText(body)
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
        uiController.displayToolbarTitle(getNoteTitle(), true)
    }

    private fun transitionToExpandedMode(){
        note_title.fadeIn()
        uiController.displayToolbarTitle(null, true)
    }

    override fun inject() {
        getNoteComponent()?.inject(this)
        printLogD("NoteDetailFragment", "injecting into component: ${getNoteComponent()}")
    }



}














