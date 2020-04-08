package com.codingwithmitch.cleannotes.notes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.NoteListViewModel
import com.codingwithmitch.cleannotes.notes.framework.presentation.NoteViewModelFactory
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.NoteDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@Module
abstract class NoteViewModelModule{

    @FeatureScope
    @Binds
    abstract fun bindViewModelFactory(factory: NoteViewModelFactory): ViewModelProvider.Factory

    @FeatureScope
    @Binds
    @IntoMap
    @NoteViewModelKey(NoteListViewModel::class)
    abstract fun bindNoteListViewModel(viewModel: NoteListViewModel): ViewModel

    @FeatureScope
    @Binds
    @IntoMap
    @NoteViewModelKey(NoteDetailViewModel::class)
    abstract fun bindNoteDetailViewModel(viewModel: NoteDetailViewModel): ViewModel

}