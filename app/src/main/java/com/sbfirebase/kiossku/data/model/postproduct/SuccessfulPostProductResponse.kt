package com.sbfirebase.kiossku.data.model.postproduct

import com.google.gson.annotations.SerializedName
import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto

data class SuccessfulPostProductResponse (
    @field:SerializedName("data")
    val data: KiosDataDto? = null,

    @field:SerializedName("message")
    val message: String? = null
)
