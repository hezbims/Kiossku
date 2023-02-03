package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.user.UpdateUserDto
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.model.UserData

interface IUserRepository {
    suspend fun getUser(userId : Int , token : String) : AuthorizedApiResponse<UserData>

    suspend fun updateUser(userId : Int , userNewData : UpdateUserDto) : AuthorizedApiResponse<Nothing>
}