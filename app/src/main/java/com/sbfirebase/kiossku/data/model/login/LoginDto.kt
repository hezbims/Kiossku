package com.sbfirebase.kiossku.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginDto(
	@field:SerializedName("data")
	val loginData: LoginData,

	@field:SerializedName("message")
	val message: String
)

data class LoginData(

	@field:SerializedName("telepon")
	val telepon: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String
)
