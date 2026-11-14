package com.itpens.kiossku.data.model.auth

import com.itpens.kiossku.domain.model.ExternalSystemError

sealed class FailedRefreshTokenError {
    /**
     * When client send invalid refresh token in request (malformed, or expired)
     */
    object InvalidRefreshToken : FailedRefreshTokenError()

    data class Other(val error: ExternalSystemError) : FailedRefreshTokenError()
}