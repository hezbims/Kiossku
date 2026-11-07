package com.itpens.kiossku.data.model.postproduct

import com.google.gson.annotations.SerializedName
import com.itpens.kiossku.data.model.getproduct.KiosDataDto

data class PostProductDto (
    @field:SerializedName("data")
    val data: KiosDataDto? = null,

    @field:SerializedName("message")
    val message: String? = null
)
