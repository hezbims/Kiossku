package com.itpens.kiossku.di

import com.itpens.kiossku.data.mapper.ErrorBodyToMessageMapper
import com.itpens.kiossku.data.mapper.GetUserMapper
import com.itpens.kiossku.data.model.user.GetUserDto
import com.itpens.kiossku.domain.mapper.Mapper
import com.itpens.kiossku.domain.model.UserData
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