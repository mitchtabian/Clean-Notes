package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.core.business.state.MessageType
import com.codingwithmitch.cleannotes.core.business.state.Response
import com.codingwithmitch.cleannotes.core.business.state.StateMessage
import com.codingwithmitch.cleannotes.core.business.state.UIComponentType
import com.codingwithmitch.cleannotes.core.framework.COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD
import com.codingwithmitch.cleannotes.core.framework.CollapsingToolbarState.*
import com.codingwithmitch.cleannotes.core.framework.fadeIn
import com.codingwithmitch.cleannotes.core.framework.fadeOut
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.framework.presentation.BaseNoteFragment
import com.codingwithmitch.cleannotes.notes.framework.presentation.NoteViewModel
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteStateEvent.CreateStateMessageEvent
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteStateEvent.InsertNewNoteEvent
import com.codingwithmitch.notes.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.layout_note_detail_toolbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
        onRestoreInstanceState(savedInstanceState)
        setupUI()
        setupOnBackPressDispatcher()

        container_due_date.setOnClickListener {
            // TODO("handle click of due date")
        }

        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null){
                viewState.noteDetailViewState.let { noteDetailViewState ->
                    noteDetailViewState.note?.let { note ->
                        setNoteTitle(note.title)
                    }

                    noteDetailViewState.collapsingToolbarState.let { state ->

                        when(state){

                            is Expanded -> {
                                transitionToExpandedMode()
                            }

                            is Collapsed -> {
                                transitionToCollapsedMode()
                            }
                        }
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

    private fun onRestoreInstanceState(savedInstanceState: Bundle?){
        // One time check for after rotation
        if(viewModel.viewState.value
                ?.noteDetailViewState
                ?.collapsingToolbarState.toString().equals(Collapsed().toString())){
            app_bar.setExpanded(false)
        }
        else{
            app_bar.setExpanded(true)
        }
    }

    private fun setupUI(){
        uiController.displayBottomNav(false)

        app_bar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener{appBar, offset ->

                if(offset < COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD){
                    viewModel.setCollapsingToolbarState(Collapsed())
                }
                else{
                    viewModel.setCollapsingToolbarState(Expanded())
                }
            })

        toolbar_back_arrow.setOnClickListener {
            onBackPressed()
        }

    }


    private fun onBackPressed(){
//        saveNote()
//        findNavController().popBackStack()

    }

    private fun setupOnBackPressDispatcher(){
        val callback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun checkNoteTitleNotNull(): String? {
        val title = note_title.text
        if(title.isNullOrBlank()){
            viewModel.setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = getString(R.string.text_note_title_cannot_be_empty),
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
            return null
        }
        else{
            return title.toString()
        }
    }

    private fun saveNote() {
        val title = checkNoteTitleNotNull()
        if(title != null){
            viewModel.setStateEvent(
                InsertNewNoteEvent(
                    title = title,
                    body = note_body.text.toString() // we don't check the body for null
                )
            )
        }
    }

    private fun transitionToCollapsedMode(){
        note_title.fadeOut()
        displayToolbarTitle(tool_bar_title, getNoteTitle(), true)
    }

    private fun transitionToExpandedMode(){
        note_title.fadeIn()
        displayToolbarTitle(tool_bar_title,null, true)
    }

    override fun inject() {
        getNoteComponent()?.inject(this)
        printLogD("NoteDetailFragment", "injecting into component: ${getNoteComponent()}")
    }

    private fun disableContentInteraction(view: EditText) {
        view.setKeyListener(null)
        view.setFocusable(false)
        view.setFocusableInTouchMode(false)
        view.setCursorVisible(false)
        view.clearFocus()
    }

    private fun enableContentInteraction(view: EditText) {
        view.setKeyListener(EditText(context).keyListener)
        view.setFocusable(true)
        view.setFocusableInTouchMode(true)
        view.setCursorVisible(true)
        view.requestFocus()
    }



}














