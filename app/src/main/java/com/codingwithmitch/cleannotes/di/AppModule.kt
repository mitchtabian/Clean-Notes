package com.codingwithmitch.cleannotes.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.codingwithmitch.cleannotes.framework.datasource.cache.abstraction.NoteCacheDataSource
import com.codingwithmitch.cleannotes.business.data.abstraction.NoteNetworkDataSource
import com.codingwithmitch.cleannotes.business.data.implementation.NoteRepositoryImpl
import com.codingwithmitch.cleannotes.business.data.abstraction.NoteRepository
import com.codingwithmitch.cleannotes.business.domain.model.NoteFactory
import com.codingwithmitch.cleannotes.business.interactors.common.DeleteNote
import com.codingwithmitch.cleannotes.business.interactors.notedetail.NoteDetailInteractors
import com.codingwithmitch.cleannotes.business.interactors.notedetail.UpdateNote
import com.codingwithmitch.cleannotes.business.interactors.notelist.*
import com.codingwithmitch.cleannotes.business.util.DateUtil
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDao
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDatabase
import com.codingwithmitch.cleannotes.framework.datasource.cache.database.NoteDatabase.Companion.DATABASE_NAME
import com.codingwithmitch.cleannotes.framework.datasource.cache.implementation.NoteCacheDataSourceImpl
import com.codingwithmitch.cleannotes.framework.datasource.cache.mappers.CacheMapper
import com.codingwithmitch.cleannotes.framework.datasource.network.database.FirebaseFirestoreConfig
import com.codingwithmitch.cleannotes.framework.datasource.network.implementation.NoteNetworkDataSourceImpl
import com.codingwithmitch.cleannotes.framework.datasource.network.mappers.NetworkMapper
import com.codingwithmitch.cleannotes.framework.datasource.preferences.PreferenceKeys
import com.codingwithmitch.cleannotes.framework.presentation.BaseApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
object AppModule {



    @JvmStatic
    @Singleton
    @Provides
    fun provideDateFormat(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDateUtil(dateFormat: SimpleDateFormat): DateUtil {
        return DateUtil(dateFormat)
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
    fun provideNoteCacheMapper(dateUtil: DateUtil): CacheMapper {
        return CacheMapper(dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteNetworkMapper(dateUtil: DateUtil): NetworkMapper {
        return NetworkMapper(dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
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
    fun provideNoteCacheDataSource(
        noteDao: NoteDao,
        noteEntityMapper: CacheMapper,
        dateUtil: DateUtil
    ): NoteCacheDataSource {
        return NoteCacheDataSourceImpl(noteDao, noteEntityMapper, dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideFirebaseFirestoreConfig(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): FirebaseFirestoreConfig{
        return FirebaseFirestoreConfig(firebaseAuth, firebaseFirestore)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteNetworkDataSource(
        firebaseFirestoreConfig: FirebaseFirestoreConfig,
        networkMapper: NetworkMapper,
        dateUtil: DateUtil
    ): NoteNetworkDataSource {
        return NoteNetworkDataSourceImpl(firebaseFirestoreConfig, networkMapper, dateUtil)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteRepository(
        noteCacheDataSource: NoteCacheDataSource,
        noteNetworkDataSource: NoteNetworkDataSource
    ): NoteRepository {
        return NoteRepositoryImpl(noteCacheDataSource, noteNetworkDataSource)
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