package com.codingwithmitch.cleannotes.di

import com.codingwithmitch.cleannotes.core.business.DateUtil
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
}