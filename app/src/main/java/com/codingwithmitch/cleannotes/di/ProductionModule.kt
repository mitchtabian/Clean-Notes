package com.codingwithmitch.cleannotes.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDatabase
import com.codingwithmitch.cleannotes.framework.datasource.preferences.PreferenceKeys
import com.codingwithmitch.cleannotes.framework.presentation.BaseApplication
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton


/*
    Dependencies in this class have test fakes for ui tests. See "TestModule.kt" in
    androidTest dir
 */
@ExperimentalCoroutinesApi
@FlowPreview
@Module
object ProductionModule {


    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteDb(app: BaseApplication): NoteDatabase {
        return Room
            .databaseBuilder(app, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPreferences(
        application: BaseApplication
    ): SharedPreferences {
        return application
            .getSharedPreferences(
                PreferenceKeys.NOTE_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }
}












