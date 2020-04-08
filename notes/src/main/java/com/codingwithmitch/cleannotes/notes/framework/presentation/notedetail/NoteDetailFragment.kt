package com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.cleannotes.core.R.drawable
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.framework.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.CollapsingToolbarState.*
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.UpdateNote.Companion.UPDATE_NOTE_FAILED_PK
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.UpdateNote.Companion.UPDATE_NOTE_SUCCESS
import com.codingwithmitch.cleannotes.notes.framework.presentation.BaseNoteFragment
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteDetailStateEvent.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state.NoteInteractionState.*
import com.codingwithmitch.notes.R
import com.google.android.material.appbar.AppBarLayout
import com.yydcdut.markdown.MarkdownEditText
import com.yydcdut.markdown.MarkdownProcessor
import com.yydcdut.markdown.syntax.edit.EditFactory
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

    val viewModel: NoteDetailViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var noteTitle: EditText
    private lateinit var noteBody: MarkdownEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            args.getParcelable<Note>(NOTE_DETAIL_SELECTED_NOTE_BUNDLE_KEY)?.let { note ->
                viewModel.setNote(note)
            }?: onErrorRetrievingNoteFromBundle()
        }
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

        noteTitle = view.findViewById(R.id.note_title)
        noteBody = view.findViewById(R.id.note_body)

        onRestoreInstanceState(savedInstanceState)
        setupUI()
        setupOnBackPressDispatcher()
        subscribeObservers()

        container_due_date.setOnClickListener {
            // TODO("handle click of due date")
        }

        noteTitle.setOnClickListener {
            onClickNoteTitle()
        }

        noteBody.setOnClickListener {
            onClickNoteBody()
        }

        setupMarkdown()
    }

    private fun setupMarkdown(){
        activity?.run {
            val markdownProcessor = MarkdownProcessor(this)
            markdownProcessor.factory(EditFactory.create())
            markdownProcessor.live(noteBody)
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
        updateNote()
    }

    override fun onPause() {
        super.onPause()
        updateAndSave()
    }


    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null){

                viewState.note?.let { note ->
                    printLogD("NoteDetailFragment", "SETTING NOTE")
//                    setNoteTitle(note.title)
//                    setNoteBody(note.body)
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
                    noteTitle.enableContentInteraction()
                    view?.showKeyboard()
                    displayEditStateToolbar()
                }

                is DefaultState -> {
                    noteTitle.disableContentInteraction()
                }
            }
        })

        viewModel.noteBodyInteractionState.observe(viewLifecycleOwner, Observer { state ->

            when(state){

                is EditState -> {
                    noteBody.enableContentInteraction()
                    view?.showKeyboard()
                    displayEditStateToolbar()
                }

                is DefaultState -> {
                    noteBody.disableContentInteraction()
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
        printLogD("DetailFragment", "setNoteTitle: ${title}")
        noteTitle.setText(title)
    }

    private fun getNoteTitle(): String{
        return noteTitle.text.toString()
    }

    private fun getNoteBody(): String {
        return noteBody.text.toString()
    }

    private fun setNoteBody(body: String?){
        printLogD("DetailFragment", "setNoteBody: ${body}")
        noteBody.setText(body)
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
        noteTitle.disableContentInteraction()
        noteBody.disableContentInteraction()
        displayDefaultToolbar()
        transitionToExpandedMode()

        app_bar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener{_, offset ->

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
        val title = noteTitle.text
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

    private fun updateNote() {
        val title = checkNoteTitleNotNull()
        if (title != null) {
            viewModel.setStateEvent(
                UpdateNoteEvent(
                    newTitle = title,
                    newBody = getNoteBody() // we don't check the body for null
                )
            )
        }
    }

    private fun transitionToCollapsedMode() {
        noteTitle.fadeOut()
        displayToolbarTitle(tool_bar_title, getNoteTitle(), true)
    }

    private fun transitionToExpandedMode() {
        noteTitle.fadeIn()
        displayToolbarTitle(tool_bar_title, null, true)
    }

    override fun inject() {
        getNoteComponent()?.inject(this)
        printLogD("NoteDetailFragment", "injecting into component: ${getNoteComponent()}")
    }

}














