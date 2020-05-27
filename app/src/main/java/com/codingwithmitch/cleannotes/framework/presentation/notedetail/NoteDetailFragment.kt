package com.codingwithmitch.cleannotes.framework.presentation.notedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.framework.presentation.common.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

const val NOTE_DETAIL_STATE_BUNDLE_KEY = "com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.state"

@FlowPreview
@ExperimentalCoroutinesApi
class NoteDetailFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseNoteFragment(R.layout.fragment_note_detail) {

    val viewModel: NoteDetailViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun inject() {
        // TODO("prepare dagger")
    }


}














