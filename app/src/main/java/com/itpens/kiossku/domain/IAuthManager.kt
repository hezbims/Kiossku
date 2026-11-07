package com.itpens.kiossku.domain

import com.itpens.kiossku.data.model.login.LoginDto
import com.itpens.kiossku.data.model.register.RegisterPost
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IAuthManager {
    fun getUserId() : Int
    fun isLoggedIn() : Boolean

    suspend fun getToken() : ApiResponse<String>

    suspend fun logOut() : ApiResponse<Nothing>

    suspend fun login(email : String, password : String) : ApiResponse<LoginDto>

    suspend fun register(registerBody : RegisterPost) : Flow<ApiResponse<Nothing>>
}