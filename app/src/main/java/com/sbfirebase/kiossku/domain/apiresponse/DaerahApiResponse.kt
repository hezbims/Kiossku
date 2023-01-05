package com.sbfirebase.kiossku.domain.apiresponse

import com.sbfirebase.kiossku.data.api.Daerah

sealed class DaerahApiResponse{
    object Loading : DaerahApiResponse()
    object Failed : DaerahApiResponse()
    class Success(val data : List<Daerah>) : DaerahApiResponse()
}
