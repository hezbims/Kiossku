package com.itpens.kiossku.data.mapper

import com.itpens.kiossku.domain.model.ExternalSystemError

fun Int.toExternalSystemErrorModel() : ExternalSystemError {
    return when(this) {
        503, 429 -> ExternalSystemError.ServiceUnavailable
        401 -> ExternalSystemError.UnauthorizedAccess
        else -> ExternalSystemError.Unexpected
    }
}