package com.sbfirebase.kiossku.data.repository

import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.data.api.DaerahApi
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IDaerahRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DaerahRepositoryImpl @Inject constructor(
    private val daerahApiClient : DaerahApi
) : IDaerahRepository{
    override suspend fun getProvinsi(): Flow<ApiResponse<List<Daerah?>>> =
        getDaerah { daerahApiClient.getProvinsi() }

    override suspend fun getKabupaten(idProvinsi: String): Flow<ApiResponse<List<Daerah?>>> =
        getDaerah { daerahApiClient.getKabupaten(idProvinsi) }

    override suspend fun getKecamatan(idKabupaten: String): Flow<ApiResponse<List<Daerah?>>> =
        getDaerah { daerahApiClient.getKecamatan(idKabupaten) }

    override suspend fun getKelurahan(idKecamatan: String): Flow<ApiResponse<List<Daerah?>>> =
        getDaerah { daerahApiClient.getKelurahan(idKecamatan) }

    private suspend fun getDaerah(getDaerah : suspend () -> List<Daerah?>) = flow{
        emit(ApiResponse.Loading())
        try{
            val result = getDaerah()
            emit(ApiResponse
                .Success(
                    result.sortedWith(
                        comparator = compareBy(String.CASE_INSENSITIVE_ORDER) {
                            it!!.nama
                        }
                    )
                )
            )
        }catch (e : Exception){
            emit(ApiResponse.Failure())
        }
    }
}
