package com.sbfirebase.kiossku.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.sbfirebase.kiossku.data.api.PostProductApi
import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IPostProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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
    ): AuthorizedApiResponse<Nothing> =
        try{
            val images = postData.images.map{ uri ->
                context.contentResolver.openInputStream(uri)!!.use{
                    it.readBytes()
                }
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

            /*
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
             */

            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name" , postData.judulPromosi)
                .addFormDataPart("jenis" , postData.tipeProperti)
                .addFormDataPart("harga" , postData.harga.toString())
                .addFormDataPart("tipe_harga" , postData.waktuPembayaran)
                .addFormDataPart("tipe_pembayaran" , postData.fixNego)
                .addFormDataPart("sistem" , postData.sewaJual)
                .addFormDataPart("lokasi" , postData.lokasi)
                .addFormDataPart("luas_lahan" , postData.luasLahan.toString())
                .addFormDataPart("luas_bangunan" , postData.luasBangunan.toString())
                .addFormDataPart("tingkat" , postData.tingkat.toString())
                .addFormDataPart("kapasitas_listrik" , postData.kapasitasListrik.toString())
                .addFormDataPart("alamat_lengkap" , postData.alamat)
                .addFormDataPart("fasilitas" , postData.fasilitas)
                .addFormDataPart("deskripsi" , postData.deskripsi)
                .addFormDataPart("panjang" , postData.panjang.toString())
                .addFormDataPart("lebar" , postData.lebar.toString())
                .apply {
                    images.forEachIndexed{ _ , it ->
                        addFormDataPart(
                            name = "images" ,
                            filename = "${fileCounter++}" ,
                            body = it.toRequestBody("image/*".toMediaType())
                        )
                    }
                }
                .build()

            val response = postProductApiClient.postProduct(
                body = multipartBody,
                token = "Bearer $token"
            )

            if (response.isSuccessful)
                AuthorizedApiResponse.Success()
            else{
                Log.e("qqqPostData" ,  response.errorBody()!!.string())
                AuthorizedApiResponse.Failure(errorCode = response.code())
            }
        }catch (e : Exception){
            Log.e("qqqPostData" , e.localizedMessage?.toString() ?: "Unknown Error Muncul")
            AuthorizedApiResponse.Failure()
        }

    private fun Uri.toBitmap() : Bitmap =
        if (Build.VERSION.SDK_INT < 28)
            MediaStore.Images.Media.getBitmap(context.contentResolver , this)
        else
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    this
                )
            )

    private var fileCounter = 0

    private fun Bitmap.toFile() : File {
        val file = File(context.cacheDir , "${fileCounter++}")
        file.createNewFile()

        val bitmapData = with(ByteArrayOutputStream()) {
            compress(Bitmap.CompressFormat.JPEG , 0 , this)
            toByteArray()
        }

        FileOutputStream(file).use {
            it.write(bitmapData)
            it.flush()
        }

        return file
    }

}