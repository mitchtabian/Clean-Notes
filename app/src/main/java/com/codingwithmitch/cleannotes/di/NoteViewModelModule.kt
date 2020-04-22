package com.codingwithmitch.cleannotes.notes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.framework.presentation.common.NoteViewModelFactory
import com.codingwithmitch.cleannotes.framework.presentation.notedetail.NoteDetailViewModel
import com.codingwithmitch.cleannotes.framework.presentation.notelist.state.NoteListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Module
abstract class NoteViewModelModule{

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: NoteViewModelFactory): ViewModelProvider.Factory

    @Singleton
    @Binds
    @IntoMap
    @NoteViewModelKey(NoteListViewModel::class)
    abstract fun bindNoteListViewModel(viewModel: NoteListViewModel): ViewModel

    @Singleton
    @Binds
    @IntoMap
    @NoteViewModelKey(NoteDetailViewModel::class)
    abstract fun bindNoteDetailViewModel(viewModel: NoteDetailViewModel): ViewModel

}