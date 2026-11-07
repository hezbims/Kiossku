package com.itpens.kiossku.domain.mapper

interface Mapper <DataModel , DomainModel> {
    fun from(data : DataModel) : DomainModel
}
