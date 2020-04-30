package com.codingwithmitch.cleannotes.notes.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class NoteViewModelKey(val value: KClass<out ViewModel>)