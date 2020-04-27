package com.codingwithmitch.cleannotes.framework.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.framework.presentation.common.BaseNoteFragment
import com.codingwithmitch.cleannotes.framework.presentation.common.NoteNetworkSyncManager
import com.codingwithmitch.cleannotes.framework.presentation.notelist.NoteListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SplashFragment: BaseNoteFragment(R.layout.fragment_splash) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: SplashViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiController.displayProgressBar(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.hasSyncBeenExecuted().observe(viewLifecycleOwner, Observer { hasSyncBeenExecuted ->

            if(hasSyncBeenExecuted){
                navNoteListFragment()
            }
        })
    }

    private fun navNoteListFragment(){
        findNavController().navigate(R.id.action_splashFragment_to_noteListFragment)
    }

    override fun inject() {
        getAppComponent().inject(this)
    }

}




























