package com.sbfirebase.kiossku.di

import com.sbfirebase.kiossku.data.mapper.GetUserMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Singleton
    @Provides
    fun provideGetUserMapper() : GetUserMapper =
        GetUserMapper()
}