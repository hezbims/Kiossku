package com.sbfirebase.kiossku.submitkios.enumdata

enum class TipePenawaran(private val str : String) : BaseEnum {
    FIX("Fix") , NEGO("Nego");

    override fun toString() = str
    override fun getItemList() = TipePenawaran.values().asList()
}