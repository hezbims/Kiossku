package com.itpens.kiossku.data.model.refresh

import com.google.gson.annotations.SerializedName

data class SuccessfulRefreshTokenResponse(
	@field:SerializedName("data")
	val data: TokenResponseData,

	@field:SerializedName("message")
	val message: String? = null
)

data class TokenResponseData(
	/**
	 * Access token
	 */
	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("refresh_token")
	val refreshToken: String
)
