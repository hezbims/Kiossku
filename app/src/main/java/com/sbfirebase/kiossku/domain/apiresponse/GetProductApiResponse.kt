package com.sbfirebase.kiossku.domain.apiresponse

import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto

sealed class GetProductApiResponse{
    object Failed : GetProductApiResponse()
    object Unauthorized : GetProductApiResponse()
    class Success(val products : List<KiosDataDto?>) : GetProductApiResponse()
}
