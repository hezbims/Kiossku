package com.sbfirebase.kiossku.data.repository

import android.util.Log
import com.sbfirebase.kiossku.data.api.PostProductApi
import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.data.model.postproduct.SuccessfulPostProductResponse
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IPostProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostProductRepositoryImpl @Inject constructor(
    private val postProductApiClient : PostProductApi
) : IPostProductRepository {
    override suspend fun submitProduct(
        postData : PostKiosData,
        token : String
    ): Flow<AuthorizedApiResponse<SuccessfulPostProductResponse>> =
        flow{
            try{
                emit(AuthorizedApiResponse.Loading())

                val response = postProductApiClient.postProduct(postData , "Bearer $token")

                if (response.isSuccessful)
                    emit(AuthorizedApiResponse.Success(response.body()!!))
                else{
                    Log.e("qqq" ,  response.errorBody()!!.string())
                    emit(AuthorizedApiResponse.Failure())
                }
            }catch (e : Exception){
                Log.e("qqq" , e.localizedMessage)
                emit(AuthorizedApiResponse.Failure())
            }
        }
}