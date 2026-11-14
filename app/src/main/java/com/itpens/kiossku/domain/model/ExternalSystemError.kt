package com.itpens.kiossku.domain.model

/**
 * When the app try to call external system operation (e.g REST API), but error occurs
 */
sealed interface ExternalSystemError {
    object Timeout : ExternalSystemError

    object NetworkIssue : ExternalSystemError

    /**
     * e.g internal server error, bad request, unexpected status code, failed parsing, etc
     * */
    object Unexpected : ExternalSystemError

    /**
     * 503 service unavailable,
     * 429 too many request (rate limiter)
     */
    object ServiceUnavailable : ExternalSystemError

    /**
     * 401
     */
    object UnauthorizedAccess : ExternalSystemError
}