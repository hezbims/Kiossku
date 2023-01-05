package com.sbfirebase.kiossku.ui.screen.submitkios.enumdata

enum class JenisProperti(private val str : String) : BaseEnum {
    LAHAN("Lahan") , KIOS("Kios") , LAPAK("Lapak") , GUDANG("Gudang");

    override fun toString() = str
    override fun getItemList() = values().asList()
}