package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.cleannotes.core.R.drawable
import com.codingwithmitch.cleannotes.core.business.state.MessageType
import com.codingwithmitch.cleannotes.core.business.state.Response
import com.codingwithmitch.cleannotes.core.business.state.StateMessage
import com.codingwithmitch.cleannotes.core.business.state.UIComponentType
import com.codingwithmitch.cleannotes.core.framework.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.CollapsingToolbarState.*
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.framework.presentation.BaseNoteFragment
import com.codingwithmitch.cleannotes.notes.framework.presentation.NoteViewModel
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteInteractionState
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteInteractionState.*
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
        subscribeObservers()

        container_due_date.setOnClickListener {
            // TODO("handle click of due date")
        }

        note_title.setOnClickListener {
            onClickNoteTitle()
        }

        note_body.setOnClickListener { v ->
            onClickNoteBody()
        }
    }

    private fun onClickNoteTitle(){
        if(!viewModel.isEditingTitle()){
            updateAndSave()
            viewModel.setNoteInteractionTitleState(EditState())
        }
    }

    private fun onClickNoteBody(){
        if(!viewModel.isEditingBody()){
            updateAndSave()
            viewModel.setNoteInteractionBodyState(EditState())
        }
    }

    private fun onBackPressed() {
        view?.hideKeyboard()
        if(viewModel.checkEditState()){
            viewModel.exitEditState()
            displayDefaultToolbar()
            updateAndSave()
        }
        else{
            findNavController().popBackStack()
        }
    }

    private fun updateAndSave(){
        viewModel.updateNote(getNoteTitle(), getNoteBody())
        saveNote()
    }

    override fun onPause() {
        super.onPause()
        updateAndSave()
    }


    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null){

                viewState.noteDetailViewState.let { noteDetailViewState ->

                    noteDetailViewState.note?.let { note ->
                        setNoteTitle(note.title)
                        setNoteBody(note.body)
                    }

                }
            }
        })

        viewModel.collapsingToolbarState.observe(viewLifecycleOwner, Observer { state ->
            when(state){

                is Expanded -> {
                    transitionToExpandedMode()
                }

                is Collapsed -> {
                    transitionToCollapsedMode()
                }
            }
        })

        viewModel.noteTitleInteractionState.observe(viewLifecycleOwner, Observer { state ->

            when(state){

                is EditState -> {
                    note_title.enableContentInteraction()
                    view?.showKeyboard()
                    displayEditStateToolbar()

                }

                is DefaultState -> {
                    note_title.disableContentInteraction()
                }
            }
        })

        viewModel.noteBodyInteractionState.observe(viewLifecycleOwner, Observer { state ->

            when(state){

                is EditState -> {
                    note_body.enableContentInteraction()
                    view?.showKeyboard()
                    displayEditStateToolbar()
                }

                is DefaultState -> {
                    note_body.disableContentInteraction()
                }
            }
        })
    }

    private fun displayDefaultToolbar(){
        activity?.let { a ->
            toolbar_primary_icon.setImageDrawable(
                resources.getDrawable(
                    drawable.ic_arrow_back_grey_24dp,
                    a.application.theme
                )
            )
            toolbar_secondary_icon.setImageDrawable(null)
        }
    }

    private fun displayEditStateToolbar(){
        activity?.let { a ->
            toolbar_primary_icon.setImageDrawable(
                resources.getDrawable(
                    drawable.ic_close_grey_24dp,
                    a.application.theme
                )
            )
            toolbar_secondary_icon.setImageDrawable(
                resources.getDrawable(
                    drawable.ic_done_grey_24dp,
                    a.application.theme
                )
            )
        }
    }

    private fun setNoteTitle(title: String){
        note_title.setText(title)
    }

    private fun getNoteTitle(): String{
        return note_title.text.toString()
    }

    private fun getNoteBody(): String {
        return note_body.text.toString()
    }

    private fun setNoteBody(body: String?){
        note_body.setText(body)
    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle?){
        // One time check for after rotation
        if(viewModel.collapsingToolbarState.toString().equals(Collapsed().toString())){
            app_bar.setExpanded(false)
        }
        else{
            app_bar.setExpanded(true)
        }
    }

    private fun setupUI(){
        uiController.displayBottomNav(false)
        note_title.disableContentInteraction()
        note_body.disableContentInteraction()
        displayDefaultToolbar()
        transitionToExpandedMode()

        app_bar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener{appBar, offset ->

                if(offset < COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD){
                    if(viewModel.isEditingTitle()){
                        onBackPressed() // save any changes
                    }
                    viewModel.setCollapsingToolbarState(Collapsed())
                }
                else{
                    viewModel.setCollapsingToolbarState(Expanded())
                }
            })

        toolbar_primary_icon.setOnClickListener {
            if(viewModel.checkEditState()){
                view?.hideKeyboard()
                // pressing 'x'
                // -> discard any changes made to title or body
                viewModel.triggerNoteObservers()
                viewModel.exitEditState()
                displayDefaultToolbar()
            }
            else{
                onBackPressed()
            }
        }

        toolbar_secondary_icon.setOnClickListener {
            if(viewModel.checkEditState()){
                view?.hideKeyboard()
                // pressing 'checkmark'
                // -> save any changes to title or body
                updateAndSave()
                viewModel.exitEditState()
                displayDefaultToolbar()
            }
        }
    }


    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun checkNoteTitleNotNull(): String? {
        val title = note_title.text
        if (title.isNullOrBlank()) {
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
        } else {
            return title.toString()
        }
    }

    private fun saveNote() {
        val title = checkNoteTitleNotNull()
        if (title != null) {
            viewModel.setStateEvent(
                InsertNewNoteEvent(
                    title = title,
                    body = getNoteBody() // we don't check the body for null
                )
            )
        }
    }

    private fun transitionToCollapsedMode() {
        note_title.fadeOut()
        displayToolbarTitle(tool_bar_title, getNoteTitle(), true)
    }

    private fun transitionToExpandedMode() {
        note_title.fadeIn()
        displayToolbarTitle(tool_bar_title, null, true)
    }

    override fun inject() {
        getNoteComponent()?.inject(this)
        printLogD("NoteDetailFragment", "injecting into component: ${getNoteComponent()}")
    }

}














