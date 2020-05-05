package com.codingwithmitch.cleannotes.framework.presentation.notelist

import android.os.Bundle
import android.view.*
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.framework.presentation.common.BaseNoteFragment


const val NOTE_LIST_STATE_BUNDLE_KEY = "com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state"

class NoteListFragment : BaseNoteFragment(R.layout.fragment_note_list)
{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun inject() {
        TODO("prepare dagger")
    }

}










































