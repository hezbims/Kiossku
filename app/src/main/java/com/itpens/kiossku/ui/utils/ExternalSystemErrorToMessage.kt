package com.itpens.kiossku.ui.utils

import androidx.annotation.StringRes
import com.itpens.kiossku.R
import com.itpens.kiossku.domain.model.ExternalSystemError

@StringRes
fun ExternalSystemError.toMessageResId(): Int = when (this) {
    ExternalSystemError.Timeout -> R.string.external_system_error_timeout
    ExternalSystemError.NetworkIssue -> R.string.external_system_error_network_issue
    ExternalSystemError.Unexpected -> R.string.external_system_error_unexpected
    ExternalSystemError.ServiceUnavailable -> R.string.external_system_error_service_unavailable
    ExternalSystemError.UnauthorizedAccess -> R.string.external_system_error_unauthorized_access
}

fun ExternalSystemError.toUserMessage(): UiText =
    UiText.Resource(toMessageResId())
