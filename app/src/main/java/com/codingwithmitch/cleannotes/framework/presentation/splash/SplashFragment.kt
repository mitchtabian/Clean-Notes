package com.codingwithmitch.cleannotes.framework.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.R
import com.codingwithmitch.cleannotes.framework.presentation.common.BaseNoteFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
class SplashFragment
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseNoteFragment(R.layout.fragment_splash) {

    val viewModel: SplashViewModel by viewModels {
        viewModelFactory
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun inject() {
        TODO("prepare dagger")
    }

}




























