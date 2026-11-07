package com.itpens.kiossku.di

import com.itpens.kiossku.data.model.getproduct.KiosDataDto
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.use_case.GetAllProductUseCases
import com.itpens.kiossku.domain.use_case.IUseCases
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {
   @Singleton
   @Binds
   abstract fun provideGetAllProductUseCases(
       getAllProductUseCases : GetAllProductUseCases
   ) : IUseCases<ApiResponse<List<KiosDataDto?>>>
}