package com.sbfirebase.kiossku.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class Data(

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
