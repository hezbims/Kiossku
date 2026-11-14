package com.itpens.kiossku.data.repository

import android.util.Log
import com.itpens.kiossku.data.api.UserApi
import com.itpens.kiossku.data.model.user.GetUserDto
import com.itpens.kiossku.data.model.user.UpdateUserDto
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.mapper.Mapper
import com.itpens.kiossku.domain.model.UserData
import com.itpens.kiossku.domain.repo_interface.IUserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiClient : UserApi,
    private val getUserMapper : Mapper<GetUserDto , UserData>,
    private val errorBodyToMessage : Mapper<String , String>
) : IUserRepository{
    override suspend fun getUser(userId: Int): ApiResponse<UserData> =
        try{
            val response = userApiClient.getUser(userId = userId)
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
    ): ApiResponse<Nothing> {
        return try{
            val response = userApiClient.updateUser(
                userId = userId,
                body = userNewData,
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