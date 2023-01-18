package com.sbfirebase.kiossku.di

import com.sbfirebase.kiossku.data.api.AuthApi
import com.sbfirebase.kiossku.data.api.DaerahApi
import com.sbfirebase.kiossku.data.api.GetProductApi
import com.sbfirebase.kiossku.data.api.PostProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiClientModule {

    @Singleton
    @Provides
    fun provideAuthApiClient() =
        Retrofit.Builder()
            .baseUrl("https://kiossku.com/be-api/v1/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<AuthApi>()

    @Singleton
    @Provides
    fun provideDaerahApiClient() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://ibnux.github.io/data-indonesia/")
            .build()
            .create<DaerahApi>()

    @Singleton
    @Provides
    fun provideGetProductApiClient() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://kiossku.com/be-api/v1/product/")
            .build()
            .create<GetProductApi>()

    @Singleton
    @Provides
    fun providePostProductApiClient() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://kiossku.com/be-api/v1/product/")
            .build()
            .create<PostProductApi>()

}