package com.itpens.kiossku.domain.repo_interface

import com.itpens.kiossku.data.api.Daerah
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IDaerahRepository {
    suspend fun getProvinsi() : Flow<ApiResponse<List<Daerah?>>>

    suspend fun getKabupaten(idProvinsi : String) : Flow<ApiResponse<List<Daerah?>>>

    suspend fun getKecamatan(idKabupaten : String) : Flow<ApiResponse<List<Daerah?>>>

    suspend fun getKelurahan(idKecamatan : String) : Flow<ApiResponse<List<Daerah?>>>
}