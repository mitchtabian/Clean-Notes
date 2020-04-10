package com.codingwithmitch.cleannotes.notes.framework.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.codingwithmitch.cleannotes.core.framework.*
import com.codingwithmitch.cleannotes.core.util.TodoCallback
import com.codingwithmitch.cleannotes.notes.di.NotesFeatureImpl
import com.codingwithmitch.cleannotes.presentation.BaseApplication
import com.codingwithmitch.cleannotes.presentation.MainActivity
import com.codingwithmitch.cleannotes.presentation.UIController
import com.codingwithmitch.notes.di.NoteComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.ClassCastException

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseNoteFragment
constructor(
    private @LayoutRes val layoutRes: Int
): Fragment() {

    lateinit var uiController: UIController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    fun displayToolbarTitle(textView: TextView, title: String?, useAnimation: Boolean) {
        if(title != null){
            showToolbarTitle(textView, title, useAnimation)
        }
        else{
            hideToolbarTitle(textView, useAnimation)
        }
    }

    private fun hideToolbarTitle(textView: TextView, animation: Boolean){
        if(animation){
            textView.fadeOut(
                object: TodoCallback {
                    override fun execute() {
                        textView.text = ""
                    }
                }
            )
        }
        else{
            textView.text = ""
            textView.gone()
        }
    }

    private fun showToolbarTitle(
        textView: TextView,
        title: String,
        animation: Boolean
    ){
        textView.text = title
        if(animation){
            textView.fadeIn()
        }
        else{
            textView.visible()
        }
    }

    fun getNoteComponent(): NoteComponent? {
        val appComponent = (activity?.application as BaseApplication).appComponent
        val noteComponent = (appComponent.notesFeature() as NotesFeatureImpl)
            .getProvider().noteComponent
        return noteComponent
    }

    abstract fun inject()

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
        try{
            uiController = context as MainActivity
        }catch (e: ClassCastException){
            e.printStackTrace()
        }
    }
}

















