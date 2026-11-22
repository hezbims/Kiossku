package com.itpens.kiossku.di

import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.TokenManager
import com.itpens.kiossku.ui.utils.IToastDisplayer
import com.itpens.kiossku.ui.utils.ToastDisplayer
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

    @Singleton
    @Binds
    abstract fun provideToastDisplayer(
        toastDisplayer : ToastDisplayer
    ) : IToastDisplayer
}