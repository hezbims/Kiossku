package com.itpens.kiossku.di

import com.itpens.kiossku.BuildConfig
import com.itpens.kiossku.data.api.*
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

    private const val AUTH_BASE_URL = "${BuildConfig.KIOSSSKU_BASE_URL}auth/"
    private const val PRODUCT_BASE_URL = "${BuildConfig.KIOSSSKU_BASE_URL}product/"

    @Singleton
    @Provides
    fun provideAuthApiClient() =
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<AuthApi>()

    @Singleton
    @Provides
    fun provideDaerahApiClient() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.INDONESIA_REGION_BASE_URL)
            .build()
            .create<DaerahApi>()

    @Singleton
    @Provides
    fun provideGetProductApiClient() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PRODUCT_BASE_URL)
            .build()
            .create<GetProductApi>()

    @Singleton
    @Provides
    fun providePostProductApiClient() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PRODUCT_BASE_URL)
            .build()
            .create<PostProductApi>()

    @Singleton
    @Provides
    fun provideUserApiClient() =
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<UserApi>()


}