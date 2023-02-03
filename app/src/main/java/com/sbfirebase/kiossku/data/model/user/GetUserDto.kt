package com.sbfirebase.kiossku.data.model.user

import com.google.gson.annotations.SerializedName

data class GetUserDto(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
