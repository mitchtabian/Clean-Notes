package com.codingwithmitch.cleannotes.framework.presentation.notelist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.business.domain.util.DateUtil
import com.codingwithmitch.cleannotes.framework.presentation.common.BaseNoteFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


const val NOTE_LIST_STATE_BUNDLE_KEY = "com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state"

@FlowPreview
@ExperimentalCoroutinesApi
class NoteListFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dateUtil: DateUtil
): BaseNoteFragment(R.layout.fragment_note_list)
{

    val viewModel: NoteListViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun inject() {
        TODO("prepare dagger")
    }

}










































