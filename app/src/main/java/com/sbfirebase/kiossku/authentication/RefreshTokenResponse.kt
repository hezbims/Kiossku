package com.sbfirebase.kiossku.authentication

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("token")
	val token: String? = null
)
