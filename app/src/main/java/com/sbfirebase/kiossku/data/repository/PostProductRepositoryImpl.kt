package com.sbfirebase.kiossku.data.repository

import android.content.Context
import android.util.Log
import com.sbfirebase.kiossku.data.api.ContentUriRequestBody
import com.sbfirebase.kiossku.data.api.PostProductApi
import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.data.model.postproduct.SuccessfulPostProductResponse
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IPostProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostProductRepositoryImpl @Inject constructor(
    private val postProductApiClient : PostProductApi,
    @ApplicationContext private val context : Context
) : IPostProductRepository {
    override suspend fun submitProduct(
        postData : PostKiosData,
        token : String
    ): Flow<AuthorizedApiResponse<SuccessfulPostProductResponse>> =
        flow{
            try{
                emit(AuthorizedApiResponse.Loading())

                val images = postData.images.map{
                    val contentResolver = context.contentResolver!!
                    MultipartBody.Part.create(
                        ContentUriRequestBody(
                            contentResolver = contentResolver,
                            contentUri = it
                        )
                    )

                }

                /*
                val postDataMap = hashMapOf(
                    "name" to postData.judulPromosi.toRequestBody(),
                    "jenis" to postData.tipeProperti.toRequestBody(),
                    "harga" to postData.harga.toString().toRequestBody(),
                    "tipe_harga" to postData.waktuPembayaran.toRequestBody(),
                    "tipe_pembayaran" to postData.fixNego.toRequestBody(),
                    "sistem" to postData.sewaJual.toRequestBody(),
                    "lokasi" to postData.lokasi.toRequestBody(),
                    "luas_lahan" to postData.luasLahan.toString().toRequestBody(),
                    "luas_bangunan" to postData.luasBangunan.toString().toRequestBody(),
                    "tingkat" to postData.tingkat.toString().toRequestBody(),
                    "kapasitas_listrik" to postData.kapasitasListrik.toString().toRequestBody(),
                    "alamat_lengkap" to postData.alamat.toRequestBody(),
                    "fasilitas" to postData.fasilitas.toRequestBody(),
                    "deskripsi" to postData.deskripsi.toRequestBody(),
                    "panjang" to postData.panjang.toString().toRequestBody(),
                    "lebar" to postData.lebar.toString().toRequestBody()
                )

                val response = postProductApiClient.postProduct(
                    images = images,
                    partMap = postDataMap,
                    token = "Bearer $token"
                )

                 */
                val response = postProductApiClient.postProduct(
                    judulPromosi = postData.judulPromosi.toRequestBody(),
                    tipeProperti = postData.tipeProperti.toRequestBody(),
                    harga = postData.harga.toString().toRequestBody(),
                    waktuPembayaran = postData.waktuPembayaran.toRequestBody(),
                    fixNego = postData.fixNego.toRequestBody(),
                    sewaJual = postData.sewaJual.toRequestBody(),
                    lokasi = postData.lokasi.toRequestBody(),
                    luasLahan = postData.luasLahan.toString().toRequestBody(),
                    luasBangunan = postData.luasBangunan.toString().toRequestBody(),
                    tingkat = postData.tingkat.toString().toRequestBody(),
                    kapasitasListrik = postData.kapasitasListrik.toString().toRequestBody(),
                    alamat = postData.alamat.toRequestBody(),
                    fasilitas = postData.fasilitas.toRequestBody(),
                    deskripsi = postData.deskripsi.toRequestBody(),
                    panjang = postData.panjang.toString().toRequestBody(),
                    lebar = postData.lebar.toString().toRequestBody(),
                    images = images,
                    token = "Bearer $token"
                )


                if (response.isSuccessful)
                    emit(AuthorizedApiResponse.Success(response.body()!!))
                else{
                    Log.e("qqq" ,  response.errorBody()!!.string())
                    emit(AuthorizedApiResponse.Failure())
                }
            }catch (e : Exception){
                Log.e("qqq" , e.localizedMessage?.toString() ?: "Unknown Error Muncul")
                emit(AuthorizedApiResponse.Failure())
            }
        }
}