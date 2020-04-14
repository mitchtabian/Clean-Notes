package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.cleannotes.R.drawable
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.framework.*
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.DeleteNote
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.DeleteNote.Companion.DELETE_ARE_YOU_SURE
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.DeleteNote.Companion.DELETE_NOTE_PENDING
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.DeleteNote.Companion.DELETE_NOTE_SUCCESS
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.UpdateNote.Companion.UPDATE_NOTE_FAILED_PK
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.UpdateNote.Companion.UPDATE_NOTE_SUCCESS
import com.codingwithmitch.cleannotes.notes.framework.presentation.BaseNoteFragment
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.CollapsingToolbarState.Collapsed
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.CollapsingToolbarState.Expanded
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteDetailStateEvent
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteDetailStateEvent.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteDetailViewState
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteInteractionState.DefaultState
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteInteractionState.EditState
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.NOTE_LIST_STATE_BUNDLE_KEY
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.NOTE_PENDING_DELETE_BUNDLE_KEY
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState
import com.codingwithmitch.notes.R
import com.google.android.material.appbar.AppBarLayout
import com.yydcdut.markdown.MarkdownProcessor
import com.yydcdut.markdown.syntax.edit.EditFactory
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.layout_note_detail_toolbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

const val NOTE_DETAIL_STATE_BUNDLE_KEY = "com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state"


@FlowPreview
@ExperimentalCoroutinesApi
class NoteDetailFragment : BaseNoteFragment(R.layout.fragment_note_detail) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: NoteDetailViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get Note after a rotation
        savedInstanceState?.let { inState ->
            (inState.getParcelable(NOTE_DETAIL_SELECTED_NOTE_BUNDLE_KEY) as Note?)?.let{ note ->
                viewModel.setNoteFromBundle(note)
            }
        }

        // get Note after navigation
        if(viewModel.getNote() == null){
            arguments?.let { args ->
                args.getParcelable<Note>(NOTE_DETAIL_SELECTED_NOTE_BUNDLE_KEY)?.let { note ->
                    viewModel.setNoteFromBundle(note)
                }?: onErrorRetrievingNoteFromBundle()
            }
        }

        viewModel.setupChannel()
    }

    private fun onErrorRetrievingNoteFromBundle(){
        viewModel.setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = NOTE_DETAIL_ERROR_RETRIEVEING_SELECTED_NOTE,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        )
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
            onClick_noteTitle()
        }

        note_body.setOnClickListener {
            onClick_noteBody()
        }

        setupMarkdown()
    }

    private fun setupMarkdown(){
        activity?.run {
            val markdownProcessor = MarkdownProcessor(this)
            markdownProcessor.factory(EditFactory.create())
            markdownProcessor.live(note_body)
        }
    }

    private fun onClick_noteTitle(){
        if(!viewModel.isEditingTitle()){
            updateBodyInViewModel()
            updateNote()
            viewModel.setNoteInteractionTitleState(EditState())
        }
    }

    private fun onClick_noteBody(){
        if(!viewModel.isEditingBody()){
            updateTitleInViewModel()
            updateNote()
            viewModel.setNoteInteractionBodyState(EditState())
        }
    }

    private fun onBackPressed() {
        view?.hideKeyboard()
        if(viewModel.checkEditState()){
            updateBodyInViewModel()
            updateTitleInViewModel()
            updateNote()
            viewModel.exitEditState()
            displayDefaultToolbar()
        }
        else{
            findNavController().popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        updateTitleInViewModel()
        updateBodyInViewModel()
        updateNote()
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null){

                viewState.note?.let { note ->
                    setNoteTitle(note.title)
                    setNoteBody(note.body)
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            uiController.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { message ->
                if(!message.response.message.equals(UPDATE_NOTE_SUCCESS)){
                    uiController.onResponseReceived(
                        response = message.response,
                        stateMessageCallback = object: StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                    )
                    when(message.response.message){

                        UPDATE_NOTE_FAILED_PK -> {
                            findNavController().popBackStack()
                        }

                        NOTE_DETAIL_ERROR_RETRIEVEING_SELECTED_NOTE -> {
                            findNavController().popBackStack()
                        }

                        DELETE_NOTE_SUCCESS -> {
                            viewModel.setNote(null)
                            findNavController().popBackStack()
                        }

                        else -> {
                            // do nothing
                        }
                    }
                }
                else{
                    viewModel.clearStateMessage()
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
            toolbar_secondary_icon.setImageDrawable(
                resources.getDrawable(
                    drawable.ic_delete,
                    a.application.theme
                )
            )
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

    private fun setNoteTitle(title: String) {
        note_title.setText(title)
    }

    private fun getNoteTitle(): String{
        return note_title.text.toString()
    }

    private fun getNoteBody(): String{
        return note_body.text.toString()
    }

    private fun setNoteBody(body: String?){
        note_body.setText(body)
    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle?){
        savedInstanceState?.let { inState ->
            (inState[NOTE_DETAIL_STATE_BUNDLE_KEY] as NoteDetailViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }

        // One-time check after rotation
        if(viewModel.isToolbarCollapsed()){
            app_bar.setExpanded(false)
            transitionToCollapsedMode()
        }
        else{
            app_bar.setExpanded(true)
            transitionToExpandedMode()
        }
    }

    private fun updateTitleInViewModel(){
        if(viewModel.isEditingTitle()){
            viewModel.updateNoteTitle(getNoteTitle())
        }
    }

    private fun updateBodyInViewModel(){
        if(viewModel.isEditingBody()){
            viewModel.updateNoteBody(getNoteBody())
        }
    }

    private fun setupUI(){
        uiController.displayBottomNav(false)
        note_title.disableContentInteraction()
        note_body.disableContentInteraction()
        displayDefaultToolbar()
        transitionToExpandedMode()

        app_bar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener{_, offset ->

                if(offset < COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD){
                    updateTitleInViewModel()
                    if(viewModel.isEditingTitle()){
                        viewModel.exitEditState()
                        displayDefaultToolbar()
                        updateNote()
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
                updateTitleInViewModel()
                updateBodyInViewModel()
                updateNote()
                viewModel.exitEditState()
                displayDefaultToolbar()
            }
            else{
                deleteNote()
            }
        }
    }

    private fun deleteNote(){
        viewModel.setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = DELETE_ARE_YOU_SURE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object: AreYouSureCallback{
                                override fun proceed() {
                                    viewModel.getCurrentViewStateOrNew().note?.let{note ->
                                        // Create bundle arg containing note to be deleted
                                        // nav to NoteListFragment and clear backstack
                                        initiateDeleteTransaction()
                                    }
                                }

                                override fun cancel() {
                                    // do nothing
                                }
                            }
                        ),
                        messageType = MessageType.Info()
                    )
                )
            )
        )
    }

    private fun initiateDeleteTransaction(){
        val bundle = bundleOf(NOTE_PENDING_DELETE_BUNDLE_KEY to viewModel.getNote())
        findNavController().navigate(
            R.id.action_note_detail_fragment_to_noteListFragment,
            bundle
        )
    }

    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    private fun updateNote() {
        viewModel.setStateEvent(
            UpdateNoteEvent()
        )
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setNote(null) // clear out the note when leaving
        viewModel.resetCollapsingToolbarState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(NOTE_DETAIL_SELECTED_NOTE_BUNDLE_KEY, viewModel.getNote())

        val viewState = viewModel.getCurrentViewStateOrNew()
        outState.putParcelable(NOTE_DETAIL_STATE_BUNDLE_KEY, viewState)
    }
}














