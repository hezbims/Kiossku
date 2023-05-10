package com.sbfirebase.kiossku.home.data

import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IDaerahRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDaerahRepository : IDaerahRepository {
    override suspend fun getProvinsi() : Flow<ApiResponse<List<Daerah?>>> =
        flow {
            emit(
                ApiResponse.Success(
                    listOf(
                        Daerah(id = "1" , nama = "prov1"),
                        Daerah(id = "2" , nama = "prov2"),
                        Daerah(id = "3" , nama = "prov3"),
                        Daerah(id = "4" , nama = "prov4")
                    )
                )
            )
        }

    override suspend fun getKabupaten(idProvinsi: String): Flow<ApiResponse<List<Daerah?>>> =
        flow {
            emit(
                ApiResponse.Success(
                    listOf(
                        Daerah(id = "5" , nama = "kab5"),
                        Daerah(id = "6" , nama = "kab6"),
                        Daerah(id = "8" , nama = "kab8"),
                        Daerah(id = "7" , nama = "kab7")
                    )
                )
            )
        }

    override suspend fun getKecamatan(idKabupaten: String): Flow<ApiResponse<List<Daerah?>>> =
        flow {
            emit(
                ApiResponse.Success(
                    listOf(
                        Daerah(id = "9" , nama = "kec9"),
                        Daerah(id = "10" , nama = "kec10"),
                        Daerah(id = "11" , nama = "kec11"),
                        Daerah(id = "12" , nama = "kec12")
                    )
                )
            )
        }

    override suspend fun getKelurahan(idKecamatan: String): Flow<ApiResponse<List<Daerah?>>> =
        flow {
            emit(
                ApiResponse.Success(
                    listOf(
                        Daerah(id = "13" , nama = "kel13"),
                        Daerah(id = "14" , nama = "kel14"),
                        Daerah(id = "15" , nama = "kel15"),
                        Daerah(id = "16" , nama = "kel16")
                    )
                )
            )
        }
}