package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.cleannotes.core.util.printLogD
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
class NoteListFragment : Fragment(R.layout.fragment_note_list) {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: NoteViewModel by viewModels {
        viewModelFactory
    }

    lateinit var uiController: UIController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notes_title.setOnClickListener {
            findNavController().navigate(R.id.action_note_list_fragment_to_noteDetailFragment)
        }
        setupUI()

        add_new_note_fab.setOnClickListener {

        }
    }

    private fun setupUI(){
        uiController.checkBottomNav(getString(com.codingwithmitch.cleannotes.R.string.module_notes_name))
        uiController.displayBottomNav(true)
    }


    override fun onAttach(context: Context) {
        val appComponent = (activity?.application as BaseApplication).appComponent
        val noteComponent = DaggerNoteComponent.factory()
            .create(appComponent)
        noteComponent.inject(this)

        super.onAttach(context)
        try{
            uiController = context as MainActivity
        }catch (e: ClassCastException){
            e.printStackTrace()
        }
    }


}































