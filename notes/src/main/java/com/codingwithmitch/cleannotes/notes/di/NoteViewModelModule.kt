package com.codingwithmitch.cleannotes.notes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.cleannotes.notes.presentation.NoteViewModel
import com.codingwithmitch.cleannotes.notes.presentation.NoteViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NoteViewModelModule{

    @Binds
    abstract fun bindViewModelFactory(factory: NoteViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @NoteViewModelKey(NoteViewModel::class)
    abstract fun bindNoteViewModel(viewModel: NoteViewModel): ViewModel

}