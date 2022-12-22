package com.sbfirebase.kiossku.submitkios.enumdata

enum class WaktuPembayaran(private val str : String) : BaseEnum{
    TAHUNAN("/tahun"), BULANAN("/bulan");

    override fun toString() = str
    override fun getItemList() = values().asList()
}