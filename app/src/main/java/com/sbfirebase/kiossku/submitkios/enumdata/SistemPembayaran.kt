package com.sbfirebase.kiossku.submitkios.enumdata

enum class SistemPembayaran (private val str : String) : BaseEnum{
    JUAL("Jual"), SEWA("Sewa");

    override fun toString() = str
    override fun getItemList() = values().asList()
}