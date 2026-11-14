package com.itpens.kiossku.di

import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class ManagerModule {

    @Singleton
    @Binds
    abstract fun provideTokenManager(tokenManager : TokenManager) : ITokenManager
}