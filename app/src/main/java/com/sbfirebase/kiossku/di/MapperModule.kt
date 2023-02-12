package com.sbfirebase.kiossku.di

import com.sbfirebase.kiossku.data.mapper.ErrorBodyToMessageMapper
import com.sbfirebase.kiossku.data.mapper.GetUserMapper
import com.sbfirebase.kiossku.data.model.user.GetUserDto
import com.sbfirebase.kiossku.domain.mapper.Mapper
import com.sbfirebase.kiossku.domain.model.UserData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {
    @Singleton
    @Binds
    abstract fun provideGetUserMapper(mapper : GetUserMapper) : Mapper<GetUserDto , UserData>

    @Singleton
    @Binds
    abstract fun provideErrorBodyToMessageMapper(
        mapper : ErrorBodyToMessageMapper
    ) : Mapper<String , String>
}