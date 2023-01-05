package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.domain.apiresponse.DaerahApiResponse
import kotlinx.coroutines.flow.Flow

interface IDaerahRepository {
    suspend fun getProvinsi() : Flow<DaerahApiResponse>

    suspend fun getKabupaten(idProvinsi : String) : Flow<DaerahApiResponse>

    suspend fun getKecamatan(idKabupaten : String) : Flow<DaerahApiResponse>

    suspend fun getKelurahan(idKecamatan : String) : Flow<DaerahApiResponse>
}