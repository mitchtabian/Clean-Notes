package com.codingwithmitch.notes.di

import android.app.Application
import androidx.room.Room
import com.codingwithmitch.cleannotes.di.features.notes.NotesFeature
import com.codingwithmitch.cleannotes.notes.data.datasource.NoteCacheDataSource
import com.codingwithmitch.cleannotes.notes.data.repository.NoteRepositoryImpl
import com.codingwithmitch.cleannotes.notes.datasource.mappers.NoteEntityMapper
import com.codingwithmitch.cleannotes.notes.di.NotesFeatureImpl
import com.codingwithmitch.cleannotes.notes.domain.repository.NoteRepository
import com.codingwithmitch.cleannotes.presentation.BaseApplication
import com.codingwithmitch.cleannotes.util.DateUtil
import com.codingwithmitch.notes.datasource.cache.db.NoteDao
import com.codingwithmitch.notes.datasource.cache.db.NoteDatabase
import com.codingwithmitch.notes.datasource.cache.db.NoteDatabase.Companion.DATABASE_NAME
import com.codingwithmitch.notes.datasource.cache.repository.NoteCacheDataSourceImpl
import dagger.Module
import dagger.Provides

@Module
object NoteModule {

    @FeatureScope
    @Provides
    @JvmStatic
    internal fun provideFeatureImpl(featureImpl: NotesFeatureImpl): NotesFeature {
        return featureImpl
    }

//    @JvmStatic
//    @FeatureScope
//    @Provides
//    fun provideNoteFragmentFactory(someString: String): FragmentFactory{
//        return NotesFragmentFactory(someString)
//    }

    @JvmStatic
    @FeatureScope
    @Provides
    fun provideNoteDb(app: BaseApplication): NoteDatabase {
        return Room
            .databaseBuilder(app, NoteDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @FeatureScope
    @Provides
    fun provideNoteDAO(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.noteDao()
    }

    @JvmStatic
    @FeatureScope
    @Provides
    fun provideNoteEntityMapper(dateUtil: DateUtil): NoteEntityMapper{
        return NoteEntityMapper(dateUtil)
    }

    @JvmStatic
    @FeatureScope
    @Provides
    fun provideNoteCacheDataSource(
        noteDao: NoteDao,
        noteEntityMapper: NoteEntityMapper
    ): NoteCacheDataSource {
        return NoteCacheDataSourceImpl(noteDao, noteEntityMapper)
    }

    @JvmStatic
    @FeatureScope
    @Provides
    fun provideNoteRepository(
        noteCacheDataSource: NoteCacheDataSource
    ): NoteRepository {
        return NoteRepositoryImpl(noteCacheDataSource)
    }
}
















