package com.sbfirebase.kiossku.di

import com.sbfirebase.kiossku.data.repository.AuthRepositoryImpl
import com.sbfirebase.kiossku.data.repository.DaerahRepositoryImpl
import com.sbfirebase.kiossku.data.repository.GetProductRepositoryImpl
import com.sbfirebase.kiossku.data.repository.PostProductRepositoryImpl
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import com.sbfirebase.kiossku.domain.repo_interface.IDaerahRepository
import com.sbfirebase.kiossku.domain.repo_interface.IGetProductRepository
import com.sbfirebase.kiossku.domain.repo_interface.IPostProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun provideAuthRepository(authRepository: AuthRepositoryImpl) : IAuthRepository

    @Singleton
    @Binds
    abstract fun provideGetProductRepository(
        getProductRepository : GetProductRepositoryImpl
    ) : IGetProductRepository

    @Singleton
    @Binds
    abstract fun provideGetDaerahRepository(
        daerahRepository : DaerahRepositoryImpl
    ) : IDaerahRepository

    @Singleton
    @Binds
    abstract fun providePostProductRepository(
        postProductRepository : PostProductRepositoryImpl
    ) : IPostProductRepository
}