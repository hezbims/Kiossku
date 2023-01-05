package com.sbfirebase.kiossku.data.repository

import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.data.api.DaerahApi
import com.sbfirebase.kiossku.domain.apiresponse.DaerahApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IDaerahRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DaerahRepositoryImpl @Inject constructor(
    private val daerahApiClient : DaerahApi
) : IDaerahRepository{
    override suspend fun getProvinsi(): Flow<DaerahApiResponse> =
        getDaerah(daerahApiClient::getProvinsi)

    override suspend fun getKabupaten(idProvinsi: String): Flow<DaerahApiResponse> =
        getDaerah { daerahApiClient.getKabupaten(idProvinsi) }

    override suspend fun getKecamatan(idKabupaten: String): Flow<DaerahApiResponse> =
        getDaerah { daerahApiClient.getKecamatan(idKabupaten) }

    override suspend fun getKelurahan(idKecamatan: String): Flow<DaerahApiResponse> =
        getDaerah { daerahApiClient.getKelurahan(idKecamatan) }

    private fun getDaerah(getDaerah : () -> List<Daerah>) = flow{
        emit(DaerahApiResponse.Loading)
        try{
            val result = getDaerah()
            emit(DaerahApiResponse.Success(result))
        }catch (e : Exception){
            emit(DaerahApiResponse.Failed)
        }
    }
}
