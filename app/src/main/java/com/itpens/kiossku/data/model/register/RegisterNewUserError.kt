package com.itpens.kiossku.data.model.register

import com.itpens.kiossku.domain.model.ExternalSystemError

sealed class RegisterNewUserError {
    abstract val message: String

    sealed class Email : RegisterNewUserError() {
        data class Invalid(override val message: String) : Email()
        data class AlreadyRegistered(override val message: String) : Email()
    }

    sealed class PhoneNumber : RegisterNewUserError() {
        data class Invalid(override val message: String) : PhoneNumber()
        data class AlreadyRegistered(override val message: String) : PhoneNumber()
    }

    sealed class Password : RegisterNewUserError() {
        data class Invalid(override val message: String) : Password()
    }

    sealed class FullName : RegisterNewUserError() {
        data class Empty(override val message: String) : FullName()
        data class AlreadyRegistered(override val message: String) : FullName()
    }

    data class Other(
        val error: ExternalSystemError,
        override val message: String = ""
    ) : RegisterNewUserError()
}
