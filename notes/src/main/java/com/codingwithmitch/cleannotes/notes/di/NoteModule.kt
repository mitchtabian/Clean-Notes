package com.codingwithmitch.notes.di

//import androidx.room.Room
//import com.codingwithmitch.notes.data.datasource.NoteCacheDataSource
//import com.codingwithmitch.notes.data.repository.NoteRepositoryImpl
//import com.codingwithmitch.notes.datasource.cache.db.NoteDao
//import com.codingwithmitch.notes.datasource.cache.db.NoteDatabase
//import com.codingwithmitch.notes.datasource.cache.db.NoteDatabase.Companion.DATABASE_NAME
//import com.codingwithmitch.notes.datasource.cache.repository.NoteCacheDataSourceImpl
//import com.codingwithmitch.notes.datasource.mappers.NoteEntityMapper
//import com.codingwithmitch.notes.domain.repository.NoteRepository
import androidx.fragment.app.FragmentFactory
import com.codingwithmitch.cleannotes.notes.presentation.NotesFragmentFactory
import dagger.Module
import dagger.Provides

@Module
object NoteModule {

//    @JvmStatic
//    @FeatureScope
//    @Provides
//    fun provideNoteFragmentFactory(someString: String): FragmentFactory{
//        return NotesFragmentFactory(someString)
//    }

    @JvmStatic
    @FeatureScope
    @Provides
    fun provideSomeString(): String{
        return "Some sting value"
    }

//    @JvmStatic
//    @FeatureScope
//    @Provides
//    fun provideNoteDb(app: Application): NoteDatabase {
//        return Room
//            .databaseBuilder(app, NoteDatabase::class.java, DATABASE_NAME)
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//
//    @JvmStatic
//    @FeatureScope
//    @Provides
//    fun provideNoteDAO(noteDatabase: NoteDatabase): NoteDao {
//        return noteDatabase.noteDao()
//    }
//
//    @JvmStatic
//    @FeatureScope
//    @Provides
//    fun provideNoteEntityMapper(dateUtil: DateUtil): NoteEntityMapper{
//        return NoteEntityMapper(dateUtil)
//    }
//
//    @JvmStatic
//    @FeatureScope
//    @Provides
//    fun provideNoteCacheDataSource(
//        noteDao: NoteDao,
//        noteEntityMapper: NoteEntityMapper
//    ): NoteCacheDataSource {
//        return NoteCacheDataSourceImpl(noteDao, noteEntityMapper)
//    }
//
//    @JvmStatic
//    @FeatureScope
//    @Provides
//    fun provideNoteRepository(
//        noteCacheDataSource: NoteCacheDataSource
//    ): NoteRepository{
//        return NoteRepositoryImpl(noteCacheDataSource)
//    }
}
















