package com.codingwithmitch.cleannotes.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.codingwithmitch.cleannotes.business.data.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.implementation.NoteRepositoryImpl
import com.codingwithmitch.cleannotes.business.data.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.interactors.common.DeleteNote
import com.codingwithmitch.cleannotes.business.interactors.notedetail.NoteDetailInteractors
import com.codingwithmitch.cleannotes.business.interactors.notedetail.UpdateNote
import com.codingwithmitch.cleannotes.business.interactors.notelist.*
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.cache.abstraction.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDatabase
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDatabase.Companion.DATABASE_NAME
import com.codingwithmitch.cleannotes.framework.datasource.cache.implementation.NoteCacheDataSourceImpl
import com.codingwithmitch.cleannotes.framework.datasource.mappers.NoteMapper
import com.codingwithmitch.cleannotes.framework.datasource.preferences.PreferenceKeys
import com.codingwithmitch.cleannotes.framework.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {


    @JvmStatic
    @Singleton
    @Provides
    fun provideDateUtil(): DateUtil {
        return DateUtil()
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

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPrefsEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteFactory(dateUtil: DateUtil): NoteFactory {
        return NoteFactory(
            dateUtil
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteDb(app: BaseApplication): NoteDatabase {
        return Room
//            .inMemoryDatabaseBuilder(app, NoteDatabase::class.java)
            .databaseBuilder(app, NoteDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteDAO(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.noteDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteEntityMapper(dateUtil: DateUtil): NoteMapper {
        return NoteMapper(dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteCacheDataSource(
        noteDao: NoteDao,
        noteEntityMapper: NoteMapper,
        dateUtil: DateUtil
    ): NoteCacheDataSource {
        return NoteCacheDataSourceImpl(noteDao, noteEntityMapper, dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteRepository(
        noteCacheDataSource: NoteCacheDataSource
    ): NoteRepository {
        return NoteRepositoryImpl(noteCacheDataSource)
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteDetailInteractors(
        noteRepository: NoteRepository
    ): NoteDetailInteractors {
        return NoteDetailInteractors(
            DeleteNote(noteRepository),
            UpdateNote(noteRepository)
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteListInteractors(
        noteRepository: NoteRepository,
        noteFactory: NoteFactory
    ): NoteListInteractors {
        return NoteListInteractors(
            InsertNewNote(noteRepository, noteFactory),
            DeleteNote(noteRepository),
            SearchNotes(noteRepository),
            GetNumNotes(noteRepository),
            RestoreDeletedNote(noteRepository),
            DeleteMultipleNotes(noteRepository),
            InsertMultipleNotes(noteRepository)
        )
    }
}