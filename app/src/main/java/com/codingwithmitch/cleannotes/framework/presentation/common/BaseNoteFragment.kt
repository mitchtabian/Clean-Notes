package com.codingwithmitch.cleannotes.framework.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseNoteFragment
constructor(
    private @LayoutRes val layoutRes: Int
): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    abstract fun inject()

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

}

















