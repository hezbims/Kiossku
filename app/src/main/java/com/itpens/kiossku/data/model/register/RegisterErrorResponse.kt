package com.itpens.kiossku.data.model.register

import com.google.gson.annotations.SerializedName

data class RegisterErrorResponse(
    @SerializedName("errors") val errors: List<RegisterErrorItem>? = null,
    @SerializedName("message") val message: String? = null
)

data class RegisterErrorItem(
    @SerializedName("type") val type: String? = null,
    @SerializedName("message") val message: String? = null
)
