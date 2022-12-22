package com.sbfirebase.kiossku.data

import com.google.gson.annotations.SerializedName

data class GetKiosResponse(

	@field:SerializedName("data")
	val data: List<KiosData?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class KiosImage(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class KiosData(

	@field:SerializedName("kapasitas_listrik")
	val kapasitasListrik: Int? = null,

	@field:SerializedName("luas_lahan")
	val luasLahan: Int? = null,

	@field:SerializedName("images")
	val images: List<Any?>? = null,

	// sewa atau jual
	@field:SerializedName("sistem")
	val sistem: String? = null,

	@field:SerializedName("tipe_harga")
	val tipeHarga: String? = null,

	@field:SerializedName("luas_bangunan")
	val luasBangunan: Int? = null,

	@field:SerializedName("fasilitas")
	val fasilitas: String? = null,

	@field:SerializedName("alamat_lengkap")
	val alamatLengkap: String? = null,

	@field:SerializedName("tingkat")
	val tingkat: Int? = null,

	@field:SerializedName("panjang")
	val panjang: Int? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("tipe_pembayaran")
	val tipePembayaran: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("lebar")
	val lebar: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
