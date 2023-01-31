package com.sbfirebase.kiossku.data.api

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val contentUri : Uri
) : RequestBody() {
    override fun contentType(): MediaType? =
        "image/*".toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        val inputStream = contentResolver.openInputStream(contentUri) ?:
            throw IllegalArgumentException("Ada bug")

        inputStream.source().use {
            sink.writeAll(it)
        }

        inputStream.close()
    }
}