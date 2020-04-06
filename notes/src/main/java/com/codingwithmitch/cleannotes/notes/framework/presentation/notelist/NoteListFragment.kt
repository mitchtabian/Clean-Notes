package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.cleannotes.core.business.state.Response
import com.codingwithmitch.cleannotes.core.framework.DialogInputCaptureCallback
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.di.NotesFeatureImpl
import com.codingwithmitch.cleannotes.notes.framework.presentation.BaseNoteFragment
import com.codingwithmitch.cleannotes.notes.framework.presentation.NoteViewModel
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteStateEvent
import com.codingwithmitch.cleannotes.notes.framework.presentation.state.NoteStateEvent.*
import com.codingwithmitch.cleannotes.presentation.BaseApplication
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.UIController
import com.codingwithmitch.notes.R
import com.codingwithmitch.notes.di.DaggerNoteComponent
import kotlinx.android.synthetic.main.fragment_note_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.ClassCastException
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class NoteListFragment : BaseNoteFragment(R.layout.fragment_note_list) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: NoteViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

        printLogD("NoteListFragment", "viewmodel: $viewModel")

        add_new_note_fab.setOnClickListener {
            uiController.displayInputCaptureDialog(
                getString(com.codingwithmitch.cleannotes.R.string.text_enter_a_title),
                object: DialogInputCaptureCallback{
                    override fun onTextCaptured(text: String) {
                        viewModel.setNote(
                            viewModel.createNewNote(
                                title = text
                            )
                        )
                        findNavController()
                            .navigate(R.id.action_note_list_fragment_to_noteDetailFragment)
                    }

                }
            )
        }
    }

    private fun setupUI(){
        uiController.checkBottomNav(getString(com.codingwithmitch.cleannotes.R.string.module_notes_name))
        uiController.displayBottomNav(true)
    }

    override fun inject() {
        getNoteComponent()?.inject(this)
        printLogD("NoteListFragment", "injecting into component: ${getNoteComponent()}")
    }


}































