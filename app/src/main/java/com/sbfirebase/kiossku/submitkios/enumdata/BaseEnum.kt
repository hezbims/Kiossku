package com.sbfirebase.kiossku.submitkios.enumdata

interface BaseEnum {
    override fun toString() : String
    fun getItemList() : List<BaseEnum>
}