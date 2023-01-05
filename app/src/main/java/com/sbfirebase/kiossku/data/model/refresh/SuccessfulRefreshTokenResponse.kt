package com.sbfirebase.kiossku.data.model.refresh

import com.google.gson.annotations.SerializedName

data class SuccessfulRefreshTokenResponse(

	@field:SerializedName("data")
	val data: TokenData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class TokenData(

	@field:SerializedName("token")
	val token: String? = null
)
