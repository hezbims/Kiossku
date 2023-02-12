package com.sbfirebase.kiossku.data.mapper

import com.google.gson.JsonParser
import com.sbfirebase.kiossku.domain.mapper.Mapper
import javax.inject.Inject

class ErrorBodyToMessageMapper @Inject constructor() : Mapper<String , String>{
    override fun from(data : String): String {
        return with(JsonParser()) {
            parse(data)
                .asJsonObject
                .get("message")
                .asString
        }
    }
}