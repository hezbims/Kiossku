package com.sbfirebase.kiossku.data.model.getproduct

import com.google.gson.annotations.SerializedName

data class SuccessfulGetKiosResponse(

	@field:SerializedName("data")
	val data: List<KiosDataDto?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)


