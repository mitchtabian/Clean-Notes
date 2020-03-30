package com.codingwithmitch.cleannotes.notes.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.presentation.BaseApplication
import com.codingwithmitch.cleannotes.util.printLogD
import com.codingwithmitch.notes.R
import com.codingwithmitch.notes.di.DaggerNoteComponent
import com.codingwithmitch.notes.di.FeatureScope
import javax.inject.Inject


class NoteListFragment
//@Inject
constructor(
//    private val someString: String
): Fragment(R.layout.fragment_note_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        printLogD("NoteListFragment", someString)
    }

    override fun onAttach(context: Context) {
//        activity?.run {
//            val appComponent = (application as BaseApplication).appComponent
//            DaggerNoteComponent.factory().create(appComponent)
//                .inject(this@NoteListFragment)
//        }

        super.onAttach(context)
    }
}














