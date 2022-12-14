package com.sbfirebase.kiossku.authentication.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("actual")
	val actual: Int? = null,

	@field:SerializedName("field")
	val field: String? = null,

	@field:SerializedName("expected")
	val expected: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)