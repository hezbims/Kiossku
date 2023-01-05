package com.sbfirebase.kiossku.ui.screen.submitkios.enumdata

interface BaseEnum {
    override fun toString() : String
    fun getItemList() : List<BaseEnum>
}