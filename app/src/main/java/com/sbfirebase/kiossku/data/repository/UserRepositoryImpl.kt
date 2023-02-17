package com.sbfirebase.kiossku.data.repository

import android.util.Log
import com.sbfirebase.kiossku.data.api.UserApi
import com.sbfirebase.kiossku.data.model.user.GetUserDto
import com.sbfirebase.kiossku.data.model.user.UpdateUserDto
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.mapper.Mapper
import com.sbfirebase.kiossku.domain.model.UserData
import com.sbfirebase.kiossku.domain.repo_interface.IUserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiClient : UserApi,
    private val getUserMapper : Mapper<GetUserDto , UserData>,
    private val errorBodyToMessage : Mapper<String , String>
) : IUserRepository{
    override suspend fun getUser(userId: Int, token: String): ApiResponse<UserData> =
        try{
            val response = userApiClient.getUser(userId = userId , token = "Bearer $token")
            if (response.isSuccessful)
                ApiResponse.Success(
                    getUserMapper.from(data = response.body()!!)
                )
            else
                ApiResponse.Failure(errorCode = response.code())
        }catch (e : Exception){
            Log.e("qqqUserRepo" , e.localizedMessage ?: "Unknown Error")
            ApiResponse.Failure()
        }

    override suspend fun updateUser(
        userId: Int,
        userNewData: UpdateUserDto,
        token : String
    ): ApiResponse<Nothing> {
        return try{
            val response = userApiClient.updateUser(
                userId = userId,
                body = userNewData,
                token = "Bearer $token"
            )
            if (response.isSuccessful)
                ApiResponse.Success()
            else{
                val message = errorBodyToMessage.from(response.errorBody()!!.string())
                Log.e("qqqUpdateUser" , message)
                ApiResponse.Failure(
                    errorMessage = message,
                    errorCode = response.code()
                )
            }
        }catch (e : Exception){
            Log.e("qqqUpdateUser" , e.localizedMessage ?: "Unknown Error")
            ApiResponse.Failure()
        }
    }
}