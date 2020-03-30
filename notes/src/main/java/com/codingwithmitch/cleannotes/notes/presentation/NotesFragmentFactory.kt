package com.codingwithmitch.cleannotes.notes.presentation

import androidx.fragment.app.FragmentFactory
import javax.inject.Inject

class NotesFragmentFactory
//@Inject
//constructor(
//    private val someString: String
//): FragmentFactory(){
//
//    override fun instantiate(classLoader: ClassLoader, className: String) =
//
//        when(className){
//
//            NoteListFragment::class.java.name -> {
//                val fragment = NoteListFragment(someString)
//                fragment
//            }
//
//
//            else -> {
//                super.instantiate(classLoader, className)
//            }
//        }
//}