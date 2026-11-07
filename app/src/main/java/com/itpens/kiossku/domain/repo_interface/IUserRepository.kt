package com.itpens.kiossku.domain.repo_interface

import com.itpens.kiossku.data.model.user.UpdateUserDto
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.model.UserData

interface IUserRepository {
    suspend fun getUser(userId : Int , token : String) : ApiResponse<UserData>

    suspend fun updateUser(
        userId : Int ,
        userNewData : UpdateUserDto,
        token : String
    ) : ApiResponse<Nothing>
}